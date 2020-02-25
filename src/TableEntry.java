public class TableEntry {
    private String address;
    private String nextHop;
    private String cost;

    public TableEntry(String address, String nextHop, String cost) {
        this.address = address;
        this.nextHop = nextHop;
        this.cost = cost;
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
}
