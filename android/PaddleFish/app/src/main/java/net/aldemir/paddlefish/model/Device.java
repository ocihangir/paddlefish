package net.aldemir.paddlefish.model;

/**
 * Created by mustafa on 15.06.15.
 */
public class Device {

    private String deviceName;
    private int deviceIcon;


    public Device(String deviceName, int deviceIcon) {
        this.deviceName = deviceName;
        this.deviceIcon = deviceIcon;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getDeviceIcon() {
        return deviceIcon;
    }
}
