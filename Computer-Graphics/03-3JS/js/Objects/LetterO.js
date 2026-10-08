import { GraphPoints } from '../Utils/GraphPoints.js';
import { ContextRender } from '../Render/ContextRender.js';
export class LetterO {

    getLetterPath(){
        let Opath = [];
        Opath = Opath.concat(GraphPoints.cubicBezierCurve(
            {x: 40.53, y: 50.71},
            {x: 42.84, y: 50.71},
            {x: 42.93, y: 52.19},
            {x: 42.93, y: 53.42},
            50
        ));

        Opath = Opath.concat(GraphPoints.cubicBezierCurve(
            {x: 42.93, y: 53.42},
            {x: 42.93, y: 54.77},
            {x: 42.75, y: 56.56},
            {x: 40.53, y: 56.56},
            50
        ));

        Opath = Opath.concat(GraphPoints.cubicBezierCurve(
            {x: 40.53, y: 56.56},
            {x: 38.32, y: 56.56},
            {x: 37.87, y: 54.78},
            {x: 37.87, y: 53.42},
            50
        ));

        Opath = Opath.concat(GraphPoints.cubicBezierCurve(
            {x: 37.87, y: 53.42},
            {x: 37.87, y: 52.20},
            {x: 38.22, y: 50.71},
            {x: 40.53, y: 50.71},
            50
        ));

        return Opath;
    }
    getLetterHolePath(){
        let holeOpath = [];

        holeOpath = holeOpath.concat(GraphPoints.cubicBezierCurve(
            {x: 40.53, y: 51.16},
            {x: 41.27, y: 51.16},
            {x: 42.02, y: 51.35},
            {x: 42.02, y: 53.42},
            50
        ));

        holeOpath = holeOpath.concat(GraphPoints.cubicBezierCurve(
            {x: 42.02, y: 53.42},
            {x: 42.02, y: 55.50},
            {x: 41.27, y: 56.09},
            {x: 40.53, y: 56.09},
            50
        ));

        holeOpath = holeOpath.concat(GraphPoints.cubicBezierCurve(
            {x: 40.53, y: 56.09},
            {x: 39.68, y: 56.09},
            {x: 38.80, y: 55.68},
            {x: 38.80, y: 53.42},
            50
        ));

        holeOpath = holeOpath.concat(GraphPoints.cubicBezierCurve(
            {x: 38.80, y: 53.42},
            {x: 38.80, y: 51.59},
            {x: 39.55, y: 51.16},
            {x: 40.53, y: 51.16},
            50
        ));
        return holeOpath;

    }
    getGeometry(){
        let OPath = this.getLetterPath();
        let holeOpath = this.getLetterHolePath();
        return ContextRender.getExtrusion(OPath, holeOpath, 0.6);
    }
}