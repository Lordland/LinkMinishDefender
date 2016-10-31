package com.example.linkminishdefender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.view.MotionEvent;

public class CapaPerder extends CCColorLayer {

	private CCSprite enemigo;
	
	protected CapaPerder(ccColor4B color) {
		super(color);
		this.setIsTouchEnabled(true);
		iniciarMusica();
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCLabel texto = CCLabel.makeLabel("Has perdido", "DroidSans", 32);
		texto.setColor(ccColor3B.ccWHITE);
		texto.setPosition(winSize.width / 2.0f, winSize.height / 2.0f);
		addChild(texto);
		enemigo = animar();
		enemigo.setPosition(CGPoint.ccp(winSize.width / 2.0f ,winSize.height/2.0f - 40 ));
		addChild(enemigo);
		this.runAction(CCSequence.actions(CCDelayTime.action(3.0f),
				CCCallFunc.action(this, "jugarDeNuevo")));
	}
	
	private CCSprite animar() {
		CCSprite enemigo = null;
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(
				"animacionenemigo.plist");
		ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
		for (int i = 1; i <= 2; ++i) {
			CCSpriteFrameCache.sharedSpriteFrameCache();
			frames.add(CCSpriteFrameCache.spriteFrameByName("enemigo" + i
					+ ".png"));
		}
		CCAnimation base = CCAnimation.animation("basica", 0.1f, frames);
		enemigo = CCSprite.sprite(frames.get(0));
		CCAction accionBasica = CCRepeatForever.action(CCAnimate.action(base,
				true));
		enemigo.runAction(accionBasica);
		return enemigo;
	}

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CapaPerder capaPerder = new CapaPerder(ccColor4B.ccc4(0, 0, 0, 255));
		scene.addChild(capaPerder);
		return scene;
	}

	public void jugarDeNuevo() {
		CapaJuego.nivel = 1;
		CapaJuego.minish = 5;
		CapaJuego.puntos = 0;
		CCDirector.sharedDirector().replaceScene(CapaJuego.scene());

	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		jugarDeNuevo();
		return true;
	}
	
	public static void iniciarMusica() {
		/** Sonido */
		Context context = CCDirector.sharedDirector().getActivity();
		SoundEngine.sharedEngine().stopSound();
		SoundEngine.purgeSharedEngine();
		SoundEngine.sharedEngine().playSound(context, R.raw.sonidoperder, true);
	}

}
