package h5.klum;

import uwlcs452552.h5.Agent;
import uwlcs452552.h5.AgentFactory;
import uwlcs452552.h5.Tile;

public class Factory implements AgentFactory {
	public Factory() { }
	
	@Override
	public Agent getAgent(int boardSize, Tile[] deck, int maxInitSeconds, int maxDrawSeconds, int maxTurnSeconds, Object id) {
		return new AgentKlum(boardSize, deck, maxInitSeconds, maxDrawSeconds, maxTurnSeconds, id);
	}

}
