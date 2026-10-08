export class ContextRender {

    constructor() {
        this.ctx = document.getElementById("canvas").getContext("2d");

        this.ctx.draw_figure = this.draw_figure.bind(this.ctx);
        this.ctx.begin_ref = this.begin_ref.bind(this.ctx);
        this.ctx.end_ref = this.end_ref.bind(this.ctx);
        this.ctx.draw_shape = this.draw_shape.bind(this.ctx);
        this.ctx.clear = this.clear.bind(this.ctx);
    }

    begin_ref(dx, dy, sx, sy, a) {
        this.save();
        this.translate(dx, dy);
        this.scale(sx, sy);
        this.rotate(a);
    }

    end_ref() {
        this.restore();
    }

    draw_shape(points) {
        this.beginPath();
        let p0 = points[0];
        this.moveTo(p0.x, p0.y);
            for (let p of points) {
                this.lineTo(p.x, p.y);
            }
        this.lineTo(p0.x, p0.y);
        this.closePath();
    }
    clear() {
        this.fillStyle = 'white';
        this.fillRect(0, 0, 0.1 , 0.1);
    }

    draw_figure(fig) {
        if (fig.hasOwnProperty("transform")) {
            this.begin_ref(
                fig.transform.dx || 0,
                fig.transform.dy || 0,
                fig.transform.sx || 1,
                fig.transform.sy || 1,
                fig.transform.a  || 0,
            );
        }

        let path = undefined;
        if (fig.hasOwnProperty("shape")) {
            path = new Path2D();
            path.moveTo(fig.shape[0].x, fig.shape[0].y);
            for (let p of fig.shape.slice(1)) {
                path.lineTo(p.x, p.y);
            }
            path.closePath();
        }

        if (fig.hasOwnProperty("style")) {
            if (fig.style.hasOwnProperty("lineWidth")) {
                this.lineWidth = fig.style.lineWidth;
            }
            if (fig.style.hasOwnProperty("fill")) {
                this.fillStyle = fig.style.fill;
                if (path !== undefined) this.fill(path);
            }
            if (fig.style.hasOwnProperty("stroke")) {
                this.strokeStyle = fig.style.stroke;
                if (path !== undefined) this.stroke(path);
            }
        }

        if (fig.hasOwnProperty("children")) {
            for (let child of fig.children) this.draw_figure(child);
        }
        
        if (fig.hasOwnProperty("transform")) this.end_ref();

    }
}