package sowbreira.f1mane.entidades;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class ObjetoEscapada extends ObjetoPista {

	@Override
	public void desenha(Graphics2D g2d, double zoom) {
		double rad = Math.toRadians((double) getAngulo());
		AffineTransform affineTransform = AffineTransform
				.getScaleInstance(1, 1);
		GeneralPath generalPath = new GeneralPath(getExterno());
		affineTransform.setToRotation(rad,
				generalPath.getBounds().getCenterX(), generalPath.getBounds()
						.getCenterY());
		generalPath.transform(affineTransform);
		affineTransform.setToScale(zoom, zoom);
		g2d.setColor(new Color(getCorPimaria().getRed(), getCorPimaria()
				.getGreen(), getCorPimaria().getBlue(), getTransparencia()));
		g2d.fill(generalPath.createTransformedShape(affineTransform));
	}

	@Override
	public Rectangle obterArea() {
		return getExterno().getBounds();
	}

	public Point centro() {
		return new Point(posicaoQuina.x + (largura / 2), posicaoQuina.y
				+ (altura / 2));
	}

	public Ellipse2D getExterno() {
		return new Ellipse2D.Double(getPosicaoQuina().x, getPosicaoQuina().y,
				largura, altura);
	}

	public static void main(String[] args) {
		System.out.println(220 / 10);
	}
}
