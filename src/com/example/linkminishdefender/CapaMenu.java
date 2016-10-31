package com.example.linkminishdefender;

import java.io.IOException;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Button;

public class CapaMenu extends CCColorLayer {

	private CCScene scene;
	private CCSprite fondo;
	private CCSprite botonJugar;
	private CGRect areaBoton;
	private CGPoint posEvento;

	protected CapaMenu(ccColor4B color) {
		super(color);
		
		this.setIsTouchEnabled(true);
		
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		fondo = CCSprite.sprite("fondoMenu.png");
		fondo.setPosition(winSize.width / 2.0f, winSize.height / 2.0f);
		addChild(fondo);
		botonJugar = CCSprite.sprite("botonJugar.png");
		botonJugar.setPosition(winSize.width / 2.0f,
				winSize.height / 2.0f - 150f);
		addChild(botonJugar);
		
		areaBoton = CGRect.make(
				botonJugar.getPosition().x - botonJugar.getContentSize().width
						/ 2.0f,
						botonJugar.getPosition().y+300 - botonJugar.getContentSize().height
						/ 2.0f, botonJugar.getContentSize().width,
						botonJugar.getContentSize().height);
	}

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CapaMenu capaSiguienteNivel = new CapaMenu(ccColor4B.ccc4(0, 255, 0,
				100));
		scene.addChild(capaSiguienteNivel);
		return scene;
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		posEvento = CGPoint.ccp(event.getX(), event.getY());
		if (CGRect.containsPoint(areaBoton, posEvento)) {
			scene = CapaJuego.scene();
			CCDirector.sharedDirector().runWithScene(scene);
		}
		return true;
	}

}
