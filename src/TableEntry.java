public class TableEntry {
    private String address;
    private String nextHop;
    private String cost;
    private String time; // TODO: 2/25/20 change the datatype

    public TableEntry(String address, String nextHop, String cost, String time) {
        this.address = address;
        this.nextHop = nextHop;
        this.cost = cost;
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNextHop() {
        return nextHop;
    }

    public void setNextHop(String nextHop) {
        this.nextHop = nextHop;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
