/*
 * GUI.java
 * Author: Woosung Song
 *
 * Class that helps choosing what GUI frame to show,
 * What to display on the frame.
 */
package client;

import java.awt.Rectangle;

public class GUI {
  public static void repaint() {
    switch (Player.state) {
      case SEARCH_ROOM:
        break;
      case ENTER_ROOM:
        switch (Player.inputState) {
          case IN_ROOM:
            // Set room information
            Client.roomFrame.setRoomID(Player.roomID);
            Client.roomFrame.setPlayerID(Player.id);
            Client.roomFrame.setPlayerReadyOrCancel(false);

            if (Opponent.state != Opponent.State.NONE) {
              Client.roomFrame.setOpponentID(Opponent.id);
              Client.roomFrame.setOpponentReadyOrCancel(Opponent.state == Opponent.State.READY_ROOM);
            } else {
              Client.roomFrame.setOpponentID("(Waiting)");
              Client.roomFrame.setOpponentReadyOrCancel(false);
            }
            break;
          default:
            // invalid state
        }
        break;
      case READY_ROOM:
        switch (Player.inputState) {
          case IN_ROOM:
            // Set room information and readiness of the player
            Client.roomFrame.setRoomID(Player.roomID);
            Client.roomFrame.setPlayerID(Player.id);
            Client.roomFrame.setPlayerReadyOrCancel(true);

            if (Opponent.state != Opponent.State.NONE) {
              Client.roomFrame.setOpponentID(Opponent.id);
              Client.roomFrame.setOpponentReadyOrCancel(Opponent.state == Opponent.State.READY_ROOM);
            } else {
              Client.roomFrame.setOpponentID("(Waiting)");
              Client.roomFrame.setOpponentReadyOrCancel(false);
            }
            break;
          case BEFORE_GAME:
            break;
          default:
            // invalid state
        }
        break;
      case MY_TURN:
        switch (Player.inputState) {
          case IN_GAME:
            // Turn information set
            Client.gameFrame.setPlayerTurn();
            Client.gameFrame.setPutStoneErrorMsg(Player.putStoneErrorMsgResponse);
            break;
          case STONE_GAME:
            break;
          default:
            // invalid state
        }
        break;
      case NOT_MY_TURN:
        switch (Player.inputState) {
          case IN_GAME:
            // Turn information set
            Client.gameFrame.setOpponentTurn();
            break;
          default:
            // invalid state
        }
        break;
      case TERMINATED:
        break;
      case EXIT:
        break;
    }
  }

  // Make it all same position to all GUI frames
  public static void synchronizeFrameBounds() {
    Rectangle bounds;
    switch (Player.state) {
      case SEARCH_ROOM:
        bounds = Client.roomSelectFrame.getBounds();
        break;
      case ENTER_ROOM:
      case READY_ROOM:
        bounds = Client.roomFrame.getBounds();
        break;
      case MY_TURN:
      case NOT_MY_TURN:
      case TERMINATED:
      default:
        bounds = Client.gameFrame.getBounds();
        break;
    }
    Client.playerIDFrame.setBounds(bounds);
    Client.roomSelectFrame.setBounds(bounds);
    Client.roomFrame.setBounds(bounds);
    Client.gameFrame.setBounds(bounds);
  }

  // Show only the relevent GUI frame
  public static void showFrame() {
    switch (Player.state) {
      case SEARCH_ROOM:
        Client.playerIDFrame.setVisible(false);
        Client.roomSelectFrame.setVisible(true);
        Client.roomFrame.setVisible(false);
        Client.gameFrame.setVisible(false);
        break;
      case ENTER_ROOM:
      case READY_ROOM:
        Client.playerIDFrame.setVisible(false);
        Client.roomSelectFrame.setVisible(false);
        Client.roomFrame.setVisible(true);
        Client.gameFrame.setVisible(false);
        break;
      case MY_TURN:
      case NOT_MY_TURN:
      case TERMINATED:
        Client.playerIDFrame.setVisible(false);
        Client.roomSelectFrame.setVisible(false);
        Client.roomFrame.setVisible(false);
        Client.gameFrame.setVisible(true);
        break;
      default:
        Client.playerIDFrame.setVisible(false);
        Client.roomSelectFrame.setVisible(false);
        Client.roomFrame.setVisible(false);
        Client.gameFrame.setVisible(false);
        break;
    }
  }
}
