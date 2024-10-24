package org.cgclass;

import org.gear.framework.application.Application;

import static org.gear.framework.application.Application.launch;


public class Main {

    public static void main(String[] args) {
        Application app = new Game();
        launch(app);
    }

}