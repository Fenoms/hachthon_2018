package team_3.hackathon.backend.orderer;

import java.util.HashMap;

public class ProposalList<K, V>{
    private HashMap<K, V>[] proposalLists;
    private int index;
    private int numBuf;
    private int maxBlockSize;

    public ProposalList(int maxBlockSize, int numBuf){
        this.numBuf = numBuf;
        this.maxBlockSize = maxBlockSize;
        this.index = 0;



        proposalLists = new HashMap[numBuf];
        for (int i = 0; i < this.numBuf; ++i){
            proposalLists[i] = new HashMap<>();
        }
    }

    public HashMap<K, V> append(K tickID, V proposal){
        HashMap<K, V> rtn = null;
        int _index = -1;

        synchronized (this){
            if (!proposalLists[index].containsKey(tickID)) {
                proposalLists[index].put(tickID, proposal);
                if (proposalLists[index].size() >= maxBlockSize) {
                    rtn = proposalLists[index];
                    proposalLists[index] = null;
                    _index = index;
                    index = (index + 1) % numBuf;
                }
            }
        }
        if (_index != -1){
            proposalLists[_index] = new HashMap<>();
        }

        return rtn;
    }

    public HashMap<K, V> forceSwitchBuf(){
        HashMap<K, V> rtn = null;
        int _index = -1;

        synchronized (this){
            if (proposalLists[index].size() != 0){
                rtn = proposalLists[index];
                _index = index;
                index = (index + 1) % numBuf;
            }
        }

        if (_index != -1){
            proposalLists[_index] = new HashMap<>();
        }

        return rtn;
    }
}
