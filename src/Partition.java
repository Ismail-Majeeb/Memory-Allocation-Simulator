public class Partition {
    private String name = "";
    private int size = 0;
    private Process process = null;

    public void setProcess(Process process) {
        this.process = process;
    }

    public Process getProcess() {
        return process;
    }

    public String getName() {
        return name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    // Constructor
    public Partition(String name, int size) {
        this.name = name;
        this.size = size;
    }
}
