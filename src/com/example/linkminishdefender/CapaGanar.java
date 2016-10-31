package com.example.linkminishdefender;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.view.MotionEvent;

public class CapaGanar extends CCColorLayer {
	protected CapaGanar(ccColor4B color) {
		super(color);
		this.setIsTouchEnabled(true);
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCLabel texto = CCLabel.makeLabel("Siguiente Nivel", "DroidSans", 32);
		texto.setColor(ccColor3B.ccWHITE);
		texto.setPosition(winSize.width / 2.0f, winSize.height / 2.0f);
		addChild(texto);
		this.runAction(CCSequence.actions(CCDelayTime.action(3.0f),
				CCCallFunc.action(this, "siguienteNivel")));
	}

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CapaGanar capaSiguienteNivel = new CapaGanar(ccColor4B.ccc4(0, 255, 0,
				100));
		scene.addChild(capaSiguienteNivel);
		return scene;
	}

	public void siguienteNivel() {
		CapaJuego.nivel++;
		CapaJuego.minish= 3 * CapaJuego.nivel;
		CCDirector.sharedDirector().replaceScene(CapaJuego.scene());
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		siguienteNivel();
		return true;
	}
}