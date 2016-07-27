package particle;

import general.CameraZ;
import general.Map;

import java.awt.Graphics2D;

public class ParticleSystem {
	final int MAX_PARTICLE = 1000;

	int nparticle;
	Particle[] particle;

	public ParticleSystem() {
		particle = new Particle[MAX_PARTICLE];
	}

	public void update(Map map) {
		for (int i = 0; i < nparticle; i++) {
			if (particle[i].update(map, this)) {
				nparticle--;
				particle[i] = particle[nparticle];
				i--;
			}
		}
	}

	public void add(Particle p) {
		if (nparticle < MAX_PARTICLE) {
			particle[nparticle] = p;
			nparticle++;
		} else
			System.out.println("reached max particle");
	}

	public void paint(Graphics2D brush, CameraZ camera) {
		for (int i = 0; i < nparticle; i++) {
			particle[i].paint(brush, camera);
		}
	}
}
