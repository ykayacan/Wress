package com.aurora.wress.intro;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;

public class CookieThumperSample {

    public static void play(TextSurface textSurface, AssetManager assetManager) {

        final Typeface productSans = Typeface.createFromAsset(assetManager, "fonts/GARTON.TTF");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(productSans);

        Text textDaai = TextBuilder
                .create("WRESS")
                .setPaint(paint)
                .setSize(64)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.SURFACE_CENTER).build();

        Text textBraAnies = TextBuilder
                .create("\"Find your outfit!\"")
                .setPaint(paint)
                .setSize(24)
                .setAlpha(0)
                .setColor(Color.CYAN)
                .setPosition(Align.BOTTOM_OF, textDaai).build();

        textSurface.play(
                new Sequential(
                        ShapeReveal.create(textDaai, 750, SideCut.show(Side.LEFT), false),
                        new Parallel(ShapeReveal.create(textDaai, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(300), ShapeReveal.create(textDaai, 600, SideCut.show(Side.LEFT), false))),
                        new Parallel(new TransSurface(500, textBraAnies, Pivot.CENTER), ShapeReveal.create(textBraAnies, 1300, SideCut.show(Side.LEFT), false))
                )
        );

    }

}
