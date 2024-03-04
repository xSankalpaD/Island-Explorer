package ca.mcmaster.se2aa4.island.team209;

public class StateControl {
    MissionState currentMission;
    FindState finding;
    ScanState scanning;
    LandState landing;
    GroundState grounding;
    public StateControl(){}
    public void stateUpdate(){}
    public void stateUpdate(String a){}
    
   
    public enum MissionState {
        FIND, SCAN, LAND, GROUND, STOP
    }

    enum FindState {
        FLY, TEMP, SEARCH, WAIT, TURN1, TURN2, TURN3, SEARCHER, END
    }

    enum ScanState {
        PENDING, GO
    }

    enum LandState {

    }

    enum GroundState {
        
    }

}
