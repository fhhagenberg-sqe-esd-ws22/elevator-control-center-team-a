package at.fhhagenberg.sqe.ui.components;


class Floor {
    final int number;

    public Floor(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("Floor %d", number);
    }

}