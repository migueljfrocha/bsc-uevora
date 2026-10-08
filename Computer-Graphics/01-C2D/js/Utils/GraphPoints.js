export class GraphPoints {

// Returns the vertices of a unit rectangle centered at the origin
static urect() {
    return [
        {x: -1, y: -1}, 
        {x:  1, y: -1},
        {x:  1, y:  1},
        {x: -1, y:  1}
    ]
}

// Creates a pie shape by connecting the origin to the given points and back to the origin
static pie(points) {
    let o = [{x: 0, y: 0}]
    return o.concat(points).concat(o);
}

// Generates points for a sector of a circle
static sector(num_points, start_angle, end_angle) {
    let step = (end_angle - start_angle) / (num_points - 1);
    let points = new Array(num_points);
    let angle = start_angle;
    for (let i = 0; i < num_points; i++) {
        points[i] = {
            x: Math.cos(angle),
            y: Math.sin(angle)
        };
        angle += step;
    }
    return points;
}

// Generates points for a star shape
static star(n, r) {
    let points = new Array(2 * n);
    let step_angle = Math.PI / n;
    let angle = 0.0;
    for (let i = 0; i < 2 * n; i++) {
        let radius = 1.0;
        if (i % 2 == 0) {
            radius = r;
        }

        points[i] = {
            x: radius * Math.cos(angle),
            y: radius * Math.sin(angle)
        }

        angle += step_angle;
    }
    return points;
}

//Generates points for a regular polygon (nagon)
static nagon(n) {
    let points = new Array(n);
    let step_angle = 2.0 * Math.PI / n;
    let angle = 0.0;
    for (let i = 0; i < n; i++) {
        points[i] = {
            x: Math.cos(angle),
            y: Math.sin(angle)
        }
        angle += step_angle;
    }
    return points;
}

// Generates points for a unit circle
static unit_circle(n) {
    let alpha_step = 2.0 * Math.PI / n;
    let alpha = 0.0;
    let points = new Array(n);
    for (let i = 0; i < n; i++) {
        let x = Math.cos(alpha);
        let y = Math.sin(alpha);
        points[i] = { x: x, y: y };
        alpha += alpha_step;
    }
    return points;
}

// Generates points for a quadratic bezier curve with a start point, control point, end point, and number of points of the path
static quadraticBezierCurve(startPoint, controlPoint, endPoint, numPoints) {
    const points = [];
    for (let i = 0; i <= numPoints; i++) {
        const t = i / numPoints;
        const c = 1 - t;
        // parametric equation of a quadratic bezier curve
        const x = Math.pow(c, 2) * startPoint.x + 2 * c * t * controlPoint.x + Math.pow(t, 2) * endPoint.x;
        const y = Math.pow(c, 2) * startPoint.y + 2 * c * t * controlPoint.y + Math.pow(t, 2) * endPoint.y;

        points.push({ x, y });
    }
    return points;
}

//Generates points for a cubic bezier curve with a start point, two control points, end point, and number of points of the path
static cubicBezierCurve(startPoint, controlPoint1, controlPoint2, endPoint, numPoints) {
    const points = [];
    
    for (let i = 0; i <= numPoints; i++) {
        const t = i / numPoints;
        const c = 1 - t;
        // parametric equation of a cubic bezier curve
        const x = Math.pow(c, 3) * startPoint.x + 3 * Math.pow(c, 2) * t * controlPoint1.x +
                3 * c * Math.pow(t, 2) * controlPoint2.x + Math.pow(t, 3) * endPoint.x;
        
        const y = Math.pow(c, 3) * startPoint.y + 3 * Math.pow(c, 2) * t * controlPoint1.y + 
                3 * c * Math.pow(t, 2) * controlPoint2.y + Math.pow(t, 3) * endPoint.y;
        
        points.push({ x, y });
    }

    return points;
}  
  
// Generates points for an ellipse with a major axis of length a, minor axis of length b, and number of points of the path
static elipse(a, b, numPoints) {
    let points = [];

    for (let i = 0; i < numPoints; i++) {
        const theta = (i / numPoints) * 2 * Math.PI;
        //parametric equation of an ellipse
        const x = a * Math.cos(theta);
        const y = b * Math.sin(theta);
        points.push({ x, y });
    }

    return points;
}

}