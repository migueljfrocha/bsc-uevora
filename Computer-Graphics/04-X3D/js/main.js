let message = (text) => {
    let terminal = document.getElementById("terminal");
    terminal.innerText = text;
}


function new_context() {
    let x3d_root = document.getElementById("scene");

    let context = {
        x3d: x3d_root,
    }
    context.render = render_context;

    return context;
}

function render_context(model) {
    message(`STEP: ${model.age}`);
}

function new_model() {
    let model = {
        age: 0,
        background:{
            color: document.getElementById("X3D__dynamicBackground"),
            tx: document.getElementById("X3D__floorID"),
            x: 3,
            y: -17,
            z: 3,
            targetBackgroundPosition: { x: 0, y: -0.2, z: 0 },
            onTarget: false,
        },
        
        sun: {
            tx: document.getElementById("X3D__sunID"),
            x: -20,
            y: 0,
            z: -15,
            targetPosition: { x: 19, y: 0, z: -5 },
            onTarget: false,

            center: { x: 9, y: 0, z: 10 },
            angle: -Math.PI,
            light: document.getElementById("X3D__lightID"),
        },
        tree: {
            tx: document.getElementById("X3D__treesID"),
            x: 2,
            y: 18,
            z: 2,
            targetTreePosition: { x: 0, y: 0, z: 0 },
            onTarget: false,
        },
        mountain:{
            tx:document.getElementById("X3D__mountainID"),
            x: 30,
            y: 15,
            z: 30,
            targetMountainPosition: { x: 14, y: -0.18, z: 6 },
            onTarget: false,
        },
        lake: {
            tx: document.getElementById("X3D__lakeID"),
            x: 14,
            y: 15,
            z: -30,
            targetLakePosition: { x: 6, y: -0.2, z: 6 },
            onTarget: false,
        },
    };
    
    model.update = update_model;
    return model;
}

function update_model() {
    this.age += 1;
    update_position(this.mountain, this.mountain.targetMountainPosition);
    update_position(this.lake, this.lake.targetLakePosition);
    update_position(this.background, this.background.targetBackgroundPosition);
    update_position(this.tree, this.tree.targetTreePosition);
    update_position(this.sun, this.sun.targetPosition);
    if(this.lake.onTarget && this.mountain.onTarget && this.background.onTarget && this.tree.onTarget && this.sun.onTarget){
        updateSun(this);  
    } 
}

//
// Update the object's position
//
function update_position(object, targetPosition) {
    const speed = 0.005;
    const { x, y, z } = object;
    const { x: tx, y: ty, z: tz } = targetPosition;

    const newX = x + (tx - x) * speed;
    const newY = y + (ty - y) * speed;
    const newZ = z + (tz - z) * speed;

    if (Math.abs(newX - tx) < 0.01 && Math.abs(newY - ty) < 0.01 && Math.abs(newZ - tz) < 0.01) {
        object.x = tx;
        object.y = ty;
        object.z = tz;
        object.onTarget = true;
    } else {
        object.x = newX;
        object.y = newY;
        object.z = newZ;
        object.onTarget = false;
    }

    object.tx.setAttribute(
        "translation", `${object.x} ${object.y} ${object.z}`
    );

}

function updateSun(model) {
    const sun = model.sun;
    const speed = 0.003;
    const radius = 15;
    const centerX = 19;
    const centerZ = 10;

    sun.angle -= speed;

    const newX = centerX;
    const newY = radius * Math.sin(sun.angle);
    const newZ = centerZ + radius * Math.cos(sun.angle);
    
    sun.tx.setAttribute("translation", `${newX} ${newY} ${newZ}`);

    
    model.background.color.setAttribute("skyColor", getSkyColor(newY));

    const light = model.sun.light;
    light.setAttribute("location", `${newX} ${newY} ${newZ}`);
    const directionX = centerX - newX;
    const directionY = - newY; 
    const directionZ = centerZ - newZ;

    light.setAttribute("direction", `${directionX} ${directionY} ${directionZ}`);
    light.setAttribute("color", getLightColor(newY));
    light.setAttribute("shadowIntensity", newY > 0 ? "0.6" : "0");

}

function interpolateColor(y, colors) {

    if (y < colors[colors.length - 1].y) {
        return colors[colors.length - 1].color;
    }

    let color1, color2;
    for (let i = 0; i < colors.length - 1; i++) {
        if (y >= colors[i + 1].y && y <= colors[i].y) {
            color1 = colors[i];
            color2 = colors[i + 1];
            break;
        }
    }
    if (color1 && color2) {
        const t = (y - color2.y) / (color1.y - color2.y);
        const color1Components = color1.color.split(" ").map(Number);
        const color2Components = color2.color.split(" ").map(Number);
        const interpolatedColor = color1Components.map((c1, index) => {
            const c2 = color2Components[index];
            return c1 * t + c2 * (1 - t);
        }).join(" ");
        return interpolatedColor;
    }

    return colors[colors.length - 1].color;
}

function getSkyColor(y) {
    const skyColors = [
        { y: 18, color: "0.871 0.929 0.98" },
        { y: 12.5, color: "0.749 0.867 0.98" },
        { y: 4.5, color: "0.275 0.482 0.678" },
        { y: 0, color: "0.055 0.122 0.2" },
        { y: -0.25, color: "0.055 0.122 0.2" },
        { y: -2.5, color: "0.0 0.0 0.0" },
    ];

    return interpolateColor(y, skyColors);
}

function getLightColor(y) {
    const lightColors = [
        { y: 18, color: "1 1 0.8" },
        { y: 12.5, color: "1 0.9 0.7" },
        { y: 4.5, color: "1 0.7 0.5" },
        { y: 0, color: "0.5 0.3 0.2" },
        { y: -0.25, color: "0.3 0.2 0.1" },
        { y: -2.5, color: "0.1 0.1 0.1" },
    ];

    return interpolateColor(y, lightColors);
}

function start_animation() { 

    console.log("Starting Animation...");
    // Animation
    let gc = new_context();
    let model = new_model();

    let step = (ts) => {
        model.update();
        gc.render(model);
        requestAnimationFrame(step);
    }

    step();
}

function main() {
    let X3D_LOADED = 0; 
    message(`I'm alive...`);

    const X3D_FILES = ["mountainObject", "backgroundObject", "sunObject", "treesObject", "lakeObject"];

    for(let xi of X3D_FILES){
        document.getElementById(xi).onload = (ev) =>  {
            X3D_LOADED++;
        };
    }

    let wait_load = function() {
        if (X3D_LOADED === 5) {
            start_animation();
        } else {
            requestAnimationFrame(wait_load);
        }
    } 
    wait_load();
}