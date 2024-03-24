package ca.mcmaster.se2aa4.island.team209.Position;

public enum Direction {
    N,
    E,
    S,
    W;

    public Direction right() {
        switch (this) {
            case E:
                return S;
            case S:
                return W;
            case W:
                return N;
            default:
                return E;
        }
    }

    public Direction left() {
        switch (this) {
            case E:
                return N;
            case N:
                return W;
            case W:
                return S;
            default:
                return E;
        }
    }
}
