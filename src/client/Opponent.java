package client;

public class Opponent {
	static enum State {
		NONE, ENTER_ROOM, READY_ROOM, MY_TURN, NOT_MY_TURN, TERMINATED
	}

	static enum InputState {
		NONE, JOIN_ROOM, IN_ROOM, IN_GAME, STONE_GAME
	}

	public static State state = State.NONE;
	public static InputState inputState = InputState.NONE;

	public static String id;
	public static int turnID;

	public static boolean isSurrendered = false;
	public static boolean isQueryTimeout = false;
	public static boolean isStoneTimeout = false;
	public static int putStoneOutOfRangeCnt = 0;

	public static void processResponse(String response) {
		if (response.equals("query timeout")) {
			isQueryTimeout = true;
			state = State.NONE;
			inputState = InputState.NONE;
		} else if (response.equals("leave")) {
			state = State.NONE;
			inputState = InputState.NONE;
		}

		switch (state) {
		case NONE:
			switch (inputState) {
			case NONE:
				if (response.equals("join")) {
					inputState = InputState.JOIN_ROOM;
				}
				break;
			case JOIN_ROOM:
				// opponent ID joined your room
				id = response;
				state = State.ENTER_ROOM;
				inputState = InputState.IN_ROOM;
				break;
			default:
				// invalid response
			}
			break;
		case ENTER_ROOM:
			switch (inputState) {
			case IN_ROOM:
				if (response.equals("ready")) {
					state = State.READY_ROOM;
				}
				break;
			default:
				// invalid response
			}
			break;
		case READY_ROOM:
			switch (inputState) {
			case IN_ROOM:
				if (response.equals("cancel")) {
					state = State.ENTER_ROOM;
				}
				break;
			default:
				// invalid response
			}
			break;
		case MY_TURN:
			switch (inputState) {
			case IN_GAME:
				if (response.equals("stone")) {
					inputState = InputState.STONE_GAME;
				} else if (response.equals("surrender")) {
					isSurrendered = true;
				} else if (response.equals("stone timeout")) {
					isStoneTimeout = true;
				} else if (response.equals("Stone out of range (error count = 1)")) {
					putStoneOutOfRangeCnt = 1;
				} else if (response.equals("Stone out of range (error count = 2)")) {
					putStoneOutOfRangeCnt = 2;
				} else {
					// invalid response
				}
				break;
			case STONE_GAME:
				int[] rc = Client.parseCoordinates(response);
				Client.gameBoard.board[rc[0]][rc[1]] = turnID;

				state = State.NOT_MY_TURN;
				inputState = InputState.IN_GAME;
				Player.state = Player.State.MY_TURN;
				Client.gameFrame.putStone(rc[0], rc[1], turnID);
				Client.gameFrame.setTimer();
				break;
			default:
				// invalid response
			}
			break;
		case NOT_MY_TURN:
			if (response.equals("surrender")) {
				isSurrendered = true;
			} else {
				// invalid response
			}
			break;
		case TERMINATED:
			// you win, you lose, you draw
			// no need to display opponent's one
			break;
		}
	}
}