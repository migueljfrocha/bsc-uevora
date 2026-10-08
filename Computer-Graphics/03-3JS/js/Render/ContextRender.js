export class ContextRender {
    //
    // Draws the 2D extrusion of a figure with "shape" property
    //
    static draw_figure(fig) {
        let path = undefined;

        if (fig.hasOwnProperty("shape")) {
            path = new THREE.Shape();
            path.moveTo(fig.shape[0].x, fig.shape[0].y);
            for (let p of fig.shape.slice(1)) {
                path.lineTo(p.x, p.y);
            }
        }

        return path;
    }
    //
    // Draws the extrusion of the shape
    //
    static getExtrusion(Path, holePath = null, depth ) {

        let shape = this.draw_figure({ shape: Path });

        let spine = this.createSpine(depth);
        
        if(holePath != null){
            let hole = this.draw_figure({ shape: holePath });
            shape.holes.push(hole);
        }

        let parameters = {
            extrudePath: spine,
        };

        const geometry = new THREE.ExtrudeGeometry(shape, parameters);

        return geometry;
    }
    //
    // Create the spine for the extrusion
    //
    static createSpine(depth) {
        let spine = [
            new THREE.Vector3(0, -depth, 0),
            new THREE.Vector3(0, depth, 0)
        ];

        return new THREE.CatmullRomCurve3(spine);
    }
}
