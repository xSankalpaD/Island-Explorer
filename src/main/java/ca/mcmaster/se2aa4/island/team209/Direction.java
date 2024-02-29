package ca.mcmaster.se2aa4.island.team209;

public enum Direction {
    N,
    E,
    S,
    W;
    public static String toString(Direction d){
        switch (d){
            case N: return "N";
            case S: return "S";
            case W: return "W";
            default: return "E";
        }

    }
    public static Direction right(Direction d){
        switch (d){
            case E: return S;
            case S: return W;
            case W: return N;
            default: return E;
        }
    }
    public static Direction left(Direction d){
        switch (d){
            case E: return N;
            case N: return W;
            case W: return S;
            default: return E;
        }
    }
}
