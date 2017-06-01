package com.blundell.tut;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.SpiDevice;

import java.io.IOException;

public class MainActivity extends Activity {

    private static final String APA102_RGB_7_LED_SLAVE = "SPI3.1";

    private static final byte LED_START_FRAME = (byte) 0b11100000;
    private static final byte LED_BRIGHTNESS = (byte) 0b00000011;
    private static final byte LED_BRIGHT_START_BYTE = (byte) (LED_START_FRAME | LED_BRIGHTNESS);
    private static final int ZERO_BITS = 0b0;
    private static final int TRANSACTION_SIZE = 37;

    private static final class Color {
        private static final Color RED = new Color(50, 0, 0);
        private static final Color YELLOW = new Color(50, 50, 0);
        private static final Color PINK = new Color(50, 10, 12);
        private static final Color GREEN = new Color(0, 50, 0);
        private static final Color ORANGE = new Color(50, 22, 0);
        private static final Color PURPLE = new Color(50, 0, 50);
        private static final Color BLUE = new Color(0, 0, 50);

        private static final Color[] RAINBOW = {
            Color.RED, Color.YELLOW, Color.PINK,
            Color.GREEN,
            Color.ORANGE, Color.PURPLE, Color.BLUE
        };

        int r;
        int g;
        int b;

        Color(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    private SpiDevice bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManagerService service = new PeripheralManagerService();

        for (String s : service.getSpiBusList()) {
            Log.d("!!!", s);
        }

        try {
            bus = service.openSpiDevice(APA102_RGB_7_LED_SLAVE);
        } catch (IOException e) {
            throw new IllegalStateException(APA102_RGB_7_LED_SLAVE + " connection cannot be opened.", e);
        }
        try {
            bus.setMode(SpiDevice.MODE2);
//            bus.setFrequency(1_000_000); // 1Mhz
//            bus.setBitsPerWord(8);
        } catch (IOException e) {
            throw new IllegalStateException(APA102_RGB_7_LED_SLAVE + " cannot be configured.", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        byte[] data = new byte[TRANSACTION_SIZE];
        for (int i = 0; i <= 3; i++) {
            data[i] = ZERO_BITS;
        }
        int p = 4;
        for (int i = 0; i < 7; i++) {
            data[p++] = LED_BRIGHT_START_BYTE;
            Color color = Color.RAINBOW[i];
            data[p++] = (byte) color.b;
            data[p++] = (byte) color.g;
            data[p++] = (byte) color.r;
        }
        for (int i = 32; i < 37; i++) {
            data[i] = ZERO_BITS;
        }

        try {
            bus.write(data, data.length);
        } catch (IOException e) {
            throw new IllegalStateException(APA102_RGB_7_LED_SLAVE + " cannot be written to.", e);
        }

    }

    @Override
    protected void onDestroy() {
        try {
            bus.close();
        } catch (IOException e) {
            Log.e("TUT", APA102_RGB_7_LED_SLAVE + "connection cannot be closed, you may experience errors on next launch.", e);
        }
        super.onDestroy();
    }
}
