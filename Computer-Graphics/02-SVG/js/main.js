'use strict'; 

import { ContextRender } from "./Render/ContextRender.js";
import { Obstacle } from "./Objects/Obstacle.js";
import { Boat } from "./Objects/Boat.js"; 

function new_model() { 
    let maxvel = parseFloat(document.getElementById("maxPVel").value) || 2.5;
    let model = {
        age: 0,
        //
        max_vel: maxvel,
        //
        // Objects
        //
        obstacles: [],
        obstacleRate: parseFloat(document.getElementById("obstacleRate").value) || 300,
        boat: new Boat(maxvel),
        //
        // Game Mechanics
        //
        score: 0,
        isOver: false,
    };

    //
    // Event listeners for the boat
    //
    document.addEventListener("keydown", (e) => {
        switch (e.key) {
            case "a": case "A": model.boat.action = 1; break;
            case "d": case "D": model.boat.action = 2; break;
            default: model.boat.action = 0; break;
        }
    });

    document.addEventListener("keyup", (e) => {
        switch (e.key) {
            case "a": case "A": model.boat.action = 0; break;
            case "d": case "D": model.boat.action = 0; break;
            default: model.boat.action = 0; break;
        }
    });
    return model;
}

function render(model) {
    if(model.isOver) {
        return;
    }
    message(`Score: ${model.score}`);

    let background = {
        transform: {
            dx: 0,
            dy: 0,
            sx: 1,
            sy: 1,
        },
        style: {
            fill: "url(#backgroundGradient)",
            stroke: "none",
            lineWidth: 2,
        },
        linearGradient: {
            id: "backgroundGradient",
            x1: "0%",
            x2: "0%",
            y1: "0%",
            y2: "100%",
            stops: [
                { offset: "10%", color: "#189BFA" },
                { offset: "20%", color: "#5FC9F3" },
                { offset: "50%", color: "#E8F9FF" },
                { offset: "80%", color: "#5FC9F3" },
                { offset: "90%", color: "#189BFA" },  
            ],
        },
        shape: [{x: 0, y: 0},
                {x: 600, y: 0},
                {x: 600, y: 600},
                {x: 0, y: 600},
                {x: 0, y: 0}
            ],
         children: [
            model.boat.getFinalGraph(),
        ],
        
    }

    for(let xi of model.obstacles) {
        background.children.push(xi.getGraph());
    }
    
    let svg_element = ContextRender.draw_figure(background);

    gameContainer.innerHTML = svg_element.outerHTML;    
}

let message = (text) => {
    let terminal = document.getElementById("terminal");
    terminal.innerText = text;
}

function update(model) {

    if(model.isOver) {
        ended(model);
    }

    model.age += 1;
    //
    // Add obstacles with the rate defined by the user higher = less obstacles spawn
    //
    if(model.age % model.obstacleRate === 0) {
        model.obstacles.push(new Obstacle());
    }
    //
    // Remove hidden obstacles if they are out of the screen
    //
    model.obstacles = model.obstacles.filter((item) => !item.isHidden);
    //
    // Update objects
    //
    for(let xi of model.obstacles) {
        xi.update(model);
    }
    
    model.boat.update();

    return model;
}

function animate(gc, model) {
    let now = performance.now();
    let previous = performance.now();
    let step = () => {
        now = performance.now();
        if(!model.isOver) {
            model.score = Math.floor((now - previous) / 1000);
            update(model);
            gc.render(model);
            requestAnimationFrame(step);
        }else{
            ended(model);
        }
        
    }
    requestAnimationFrame(step);
}

function main() {

    let model = new_model();
    let gc = new ContextRender();
    gc.render = render;
    gc.message = message;

    animate(gc, model);
}
function ended(model) {
    message(`Game Over! Pontuação: ${model.score}`);
    console.log("Game has ended.");
    // Re-enable the start button
    document.getElementById("startButton").disabled = false; 
}


document.getElementById("startButton").onclick = function startGame() {
    // Disable the start button
    document.getElementById("startButton").disabled = true; 
    main();
}