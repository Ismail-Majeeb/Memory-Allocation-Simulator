import java.util.Scanner;
import java.util.Vector;

public class Main {
    public static Vector<Partition> partitions = new Vector<>();
    public static Vector<Partition> partitions_temp = new Vector<>();
    public static Vector<Process> processes = new Vector<>();
    public static Vector<Process> processes_temp = new Vector<>();
    public static int partition_cnt = 0;
    public static int flag = 1;

    // fill temp vectors
    public static void fill() {
        partition_cnt = partitions.size();

        partitions_temp.clear();
        for (Partition partition : partitions)
            partitions_temp.add(new Partition(partition.getName(), partition.getSize()));

        processes_temp.clear();
        for (Process process : processes) processes_temp.add(new Process(process.getName(), process.getSize()));
    }

    // First-fit algorithm
    public static void firstFit() {
        // fill temp arrays
        if (flag == 1) fill();
        // for_break is used to stop while loop
        // cnt is the right index to remove process from processes_temp vector
        int cnt = 0, for_break = processes_temp.size();
        while (for_break-- != 0) {
            Process process = processes_temp.get(cnt);
            String process_name = process.getName();
            for (int j = 0; j < partitions_temp.size(); j++) {

                Partition partition = partitions_temp.get(j);

                // if there is enough size to put current process in partition j
                if (partition.getSize() >= process.getSize() && partition.getProcess() == null) {

                    // if there is an additional size after adding process to partition
                    if (partition.getSize() > process.getSize()) {
                        int partition_size_difference = partition.getSize() - process.getSize();

                        // create new partition
                        partitions_temp.insertElementAt(new Partition("Partition " + partition_cnt, partition_size_difference), j + 1);
                        partition_cnt++;
                    }

                    // set the partition i with size of process i
                    partition.setSize(process.getSize());

                    // put current process in partition i, then remove this process from processes queue
                    partition.setProcess(process);
                    processes_temp.remove(cnt);
                    if (processes_temp.size() != 0) process = processes_temp.get(cnt);
                    break;
                }
            }
            // there is not enough size to put current process in
            if (process_name.equals(process.getName())) cnt++;
        }
    }

    // Worst-fit algorithm
    public static void worstFit() {
        if (flag == 1) fill();
        int for_break = processes_temp.size(), cnt = 0;
        while (for_break-- != 0) {

            boolean first_empty = false;
            int idx = -1;
            Process process = processes_temp.get(cnt);

            // select best-fit partition
            for (int i = 0; i < partitions_temp.size(); i++) {
                Partition partition = partitions_temp.get(i);
                // check if ith partition does not have  process
                //check if ith partition is the first empty partition
                if (partition.getSize() >= process.getSize() && partition.getProcess() == null) {

                    if (first_empty && partition.getSize() > partitions_temp.get(idx).getSize()) idx = i;
                    else if (!first_empty) {
                        first_empty = true;
                        idx = i;
                    }
                }
            }

            if (idx != -1) {
                // put partition
                // if there is an additional size after adding process to partition
                if (partitions_temp.get(idx).getSize() > process.getSize()) {
                    int partition_size_difference = partitions_temp.get(idx).getSize() - process.getSize();

                    // create new partition
                    partitions_temp.insertElementAt(new Partition("Partition " + partition_cnt, partition_size_difference), idx + 1);
                    partition_cnt++;
                }
                partitions_temp.get(idx).setSize(process.getSize());
                partitions_temp.get(idx).setProcess(process);
                processes_temp.remove(cnt);
            } else {
                cnt++;
            }
        }
    }

    // Best-fit algorithm
    public static void bestFit() {
        if (flag == 1) fill();
        int for_break = processes_temp.size(), cnt = 0;
        while (for_break-- != 0) {

            boolean first_empty = false;
            int idx = -1;
            Process process = processes_temp.get(cnt);

            // select best-fit partition
            for (int i = 0; i < partitions_temp.size(); i++) {
                Partition partition = partitions_temp.get(i);
                // check if ith partition does not have  process
                //check if ith partition is the first empty partition
                if (partition.getSize() >= process.getSize() && partition.getProcess() == null) {

                    if (first_empty && partition.getSize() < partitions_temp.get(idx).getSize()) idx = i;
                    else if (!first_empty) {
                        first_empty = true;
                        idx = i;
                    }
                }
            }

            if (idx != -1) {
                // put partition
                // if there is an additional size after adding process to partition
                if (partitions_temp.get(idx).getSize() > process.getSize()) {
                    int partition_size_difference = partitions_temp.get(idx).getSize() - process.getSize();

                    // create new partition
                    partitions_temp.insertElementAt(new Partition("Partition " + partition_cnt, partition_size_difference), idx + 1);
                    partition_cnt++;
                }
                partitions_temp.get(idx).setSize(process.getSize());
                partitions_temp.get(idx).setProcess(process);
                processes_temp.remove(cnt);
            } else {
                cnt++;
            }
        }
    }

    // Compacting memory
    public static void compact() {
        int sum = 0, cnt = 0, for_break = partitions_temp.size();
        while (for_break-- != 0) {
            if (partitions_temp.get(cnt).getProcess() == null) {
                sum += partitions_temp.get(cnt).getSize();
                partitions_temp.remove(cnt);
            } else
                cnt++;
        }
        partitions_temp.insertElementAt(new Partition("Partition " + partition_cnt, sum), partitions_temp.size());
        partition_cnt++;
    }

    // print
    public static void print() {
        for (Partition partition : partitions_temp) {
            if (partition.getProcess() != null)
                System.out.println(partition.getName() + " (" + partition.getSize() + " KB) => " + partition.getProcess().getName());
            else
                System.out.println(partition.getName() + " (" + partition.getSize() + " KB) => External fragment");
        }
        for (Process process : processes_temp)
            if (process != null)
                System.out.println(process.getName() + " can not be allocated ");
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // input partitions
        System.out.print("Enter number of partitions: ");
        int partitionsNumber = in.nextInt();
        for (int i = 0; i < partitionsNumber; i++) {
            System.out.print("Enter the partition size of partition " + i + ": ");
            int partition_size = in.nextInt();
            Partition partition = new Partition(("Partition " + partition_cnt), partition_size);
            partitions.add(partition);
            partition_cnt++;
        }

        // input processes
        System.out.print("\nEnter number of processes: ");
        int processesNumber = in.nextInt();
        for (int i = 0; i < processesNumber; i++) {
            System.out.print("Enter size of process " + (i + 1) + ": ");
            int process_size = in.nextInt();
            Process process = new Process(("Process " + (i + 1)), process_size);
            processes.add(process);
        }

        // input policies
        int compact_flag = 1;
        while (compact_flag == 1) {
            System.out.println("""
                    Select the policy you want to apply:
                    1. First fit
                    2. Worst fit
                    3. Best fit
                    """);
            int policy = in.nextInt();
            switch (policy) {
                case 1 -> firstFit();
                case 2 -> worstFit();
                case 3 -> bestFit();
            }
            print();
            System.out.println("Do you want to compact? 1.yes 2.no");
            compact_flag = in.nextInt();
            if (compact_flag == 1) {
                compact();
                flag = 0;
                switch (policy) {
                    case 1 -> firstFit();
                    case 2 -> worstFit();
                    case 3 -> bestFit();
                }
                print();
                flag = 1;
            }
        }
    }
}

/*
5
100
500
200
300
600
4
212
417
112
426
* */