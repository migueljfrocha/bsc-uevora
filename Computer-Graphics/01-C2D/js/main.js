'use strict';

import { Plane } from "./Objects/Airplane.js";
import { BackGround } from "./Objects/BackGround.js";
import { Banner } from "./Objects/Banner.js";
import { Bird } from "./Objects/Bird.js";
import { Cloud } from "./Objects/Cloud.js";

import { ContextRender } from "./Render/ContextRender.js";

function message(text) {
    let terminal = document.getElementById("terminal");
    terminal.innerHTML = text;
}

function init_model(gc) {
    let model = {

        age: 0,
        last_timestamp: performance.now(),
        dt: 0,

        birds: [],
        clouds: [],

        background: new BackGround(),
        plane: new Plane(),            
        banner: new Banner(gc),
    };

    return model;
}

function render(model, gc) {

    gc.begin_ref(0, 0, gc.canvas.width, gc.canvas.height, 0);

    message(`Age: ${model.age}`+ ` | Timestamp: ${model.dt}`);

    gc.draw_figure(model.background.getGraph(gc));

    model.clouds.forEach(cloud => {
        gc.draw_figure(cloud.getGraph());
    });

    model.birds.forEach( birds =>{
        gc.draw_figure(birds.getGraph())
    });

    //
    // Object drawing
    //
    let rope = getRopeGraph(model);
    gc.draw_figure(rope);

    let banner = model.banner.getGraph();
    gc.draw_figure(banner);

    let plane = model.plane.getGraph();
    gc.draw_figure(plane);

    gc.end_ref();  
}

function update(model) {
    let timestamp = performance.now();
    model.dt = timestamp - model.last_timestamp;
    model.last_timestamp = timestamp;

    let randomP = Math.random();
    //
    // Clouds and birds generation and removal
    //
    if (model.age % 500 === 0  && randomP > 0.2 && model.clouds.length < 7) { 
        model.clouds.push(new Cloud());
    }
    
    if (model.age % 150 === 0  && randomP > 0.5 && model.birds.length < 5) { 
        model.birds.push(new Bird());
    }

    for( let xi of model.clouds){
        if(xi.isHidden){
            model.clouds = model.clouds.filter(cloud => !cloud.isHidden);
        }
    }

    for( let xi of model.birds){
        if(xi.isHidden){
            model.birds = model.birds.filter(bird => !bird.isHidden);
        }
    }
    //
    // Update the clouds and birds
    //
    model.clouds.forEach(cloud => cloud.update(model.dt));
    model.birds.forEach(bird => bird.update(model.dt));

    model.age++;
    model.background.update();

    TWEEN.update();
    //
    // Follower movement
    //
    let dx = model.banner.speed * (model.plane.transform.dx - model.banner.transform.dx);
    let dy = model.banner.speed * (model.plane.transform.dy - model.banner.transform.dy);
    //
    model.banner.transform.dx += dx * model.dt - 0.02;
    model.banner.transform.dy += dy * model.dt + 0.006;

    return model;
}

function getRopeGraph(model) {
    return {
        transform: {
            dx: 0.15, 
            dy: 0.7, 
            sx: 1,
            sy: 1,
            a: 0,
        },
        style: {
            fill: "orange",
            stroke: "orange", 
            lineWidth: 0.002,
        },
        shape: [
            {x: model.plane.transform.dx - 0.06, y: model.plane.transform.dy - 0.51}, 
            {x: model.banner.transform.dx + 0.2, y: model.banner.transform.dy - 0.63},
        ],
    };
}
function main() {

    console.log("I'm alive");
  
    let gc = new ContextRender().ctx;
    gc.render = render;
    gc.canvas.width = 800;
    gc.canvas.height = 800;
    //
    //Model initialization
    //
    let model = init_model(gc);

        function animatePlane() {
            // Random landing position
            let landingX = Math.random() * 0.8 - 0.2;
            let landingY = 0.5;

            let planeLandTween = new TWEEN.Tween(model.plane.transform)
                .to({ dx: landingX, dy: landingY }, 5000)
                .easing(TWEEN.Easing.Quadratic.InOut);

            let planeWaitTween = new TWEEN.Tween(model.plane.transform)
                .to({}, 5000)
                .easing(TWEEN.Easing.Linear.None);

            let planeTakeoffTween = new TWEEN.Tween(model.plane.transform)
                .to({ dx: 1.5 }, 5000)
                .easing(TWEEN.Easing.Quadratic.InOut)
                .onComplete(() => {
                    //Reset the banner and plane positions
                    model.plane.transform.dx = -0.5;
                    model.plane.transform.dy = 0.42;
                    model.banner.transform.dx = -0.5;
                    model.banner.transform.dy = 0.42;
    
                    setTimeout(animatePlane, 5000);
                });
    
            planeLandTween.chain(planeWaitTween).chain(planeTakeoffTween).start();
        }
    
        animatePlane();

    const step = () => {
        update(model);
        gc.render(model, gc); 
        requestAnimationFrame(step);
    };
    
    step();
}

main();