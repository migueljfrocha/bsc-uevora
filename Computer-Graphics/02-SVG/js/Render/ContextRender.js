import { Calc } from "../Utils/Calc.js";
export class ContextRender {

    constructor() {
        this.gameContainer = document.getElementById("gameContainer");
    }
 
    static draw_figure(fig) {
        let svg;
    
        if (!fig.isChild) {
            svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
            svg.setAttribute('width', "100%");
            svg.setAttribute('height', "100%");
        }
    
        let group = document.createElementNS("http://www.w3.org/2000/svg", "g");
        
        if (fig.hasOwnProperty("transform")) {
            let transformStr = `translate(${(fig.transform.dx || 0)},${(fig.transform.dy || 0)}) scale(${fig.transform.sx || 1},${fig.transform.sy || 1}) rotate(${Calc.radiansToDegrees(fig.transform.a) || 0})`;
            group.setAttribute('transform', transformStr);
        }
        
        if (fig.hasOwnProperty("style")) {
            if (fig.style.hasOwnProperty("fill")) {
                let path = document.createElementNS("http://www.w3.org/2000/svg", "path");
                path.setAttribute('stroke-linecap', fig.style.strokeLineCap);
                path.setAttribute('stroke-linejoin', fig.style.strokeLineJoin);
                path.setAttribute('fill', fig.style.fill);
                path.setAttribute('stroke', fig.style.stroke);
                path.setAttribute('stroke-width', fig.style.lineWidth);
                path.setAttribute('d', ContextRender.build_path(fig.shape));
                group.appendChild(path);
            }
        }
        //
        // Gradients for the background
        //
        if (fig.hasOwnProperty("linearGradient")) {
            let linearGradient = document.createElementNS("http://www.w3.org/2000/svg", "linearGradient");
            linearGradient.setAttribute('id', `${fig.linearGradient.id}`);
            linearGradient.setAttribute('x1', `${fig.linearGradient.x1}`);
            linearGradient.setAttribute('x2', `${fig.linearGradient.x2}`);
            linearGradient.setAttribute('y1', `${fig.linearGradient.y1}`);
            linearGradient.setAttribute('y2', `${fig.linearGradient.y2}`);
    
            for (let stop of fig.linearGradient.stops) {
                let stopElement = document.createElementNS("http://www.w3.org/2000/svg", "stop");
                stopElement.setAttribute('offset', stop.offset);
                stopElement.setAttribute('stop-color', stop.color);
                linearGradient.appendChild(stopElement);
            }
    
            group.appendChild(linearGradient);
        }

        if (fig.hasOwnProperty("children")) {
            for (let child of fig.children) {
                child.isChild = true;
                let svg_child = ContextRender.draw_figure(child);
                group.appendChild(svg_child);
            }
        }
    
        if (!fig.isChild) {
            svg.appendChild(group);
            return svg;
        }
    
        return group;
    }
    
    static build_path(points) {
        let path_spec = "";
        if (points.length > 0) {
            let start_point = points.shift();

            path_spec = `M ${start_point.x} ${start_point.y}`;
            for (let point of points) path_spec += ` L ${point.x} ${point.y}`;
    
            path_spec += " Z";
        }
    
        return path_spec;
    }
}