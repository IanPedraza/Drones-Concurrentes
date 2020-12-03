package cpproyecto1;

import static cpproyecto1.SynchronizeOptions.SEMAPHORE;

public class Settings {

    public final int DEAFULT_SPEED = 300;
    
    private final int step = 50;
    private int synchronizeOption;
    private boolean graphic;
    private int speed;
    private boolean pause;

    private OnPauseListener onPauseListener;
    private OnSpeedChangeListener onSpeedChangeListener;
    
    public Settings() {
        this.graphic = true;
        this.synchronizeOption = SEMAPHORE;
        this.speed = DEAFULT_SPEED;
        this.pause = false;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
        if (onPauseListener != null) onPauseListener.onPause();
    }

    public void increaseSpeed() {
        if (speed - step >= 0) {
            this.speed -= step;
        }
        
        if (onSpeedChangeListener != null) onSpeedChangeListener.onChange(speed);
    }

    public void decreaseSpeed() {
        this.speed += step;
        if (onSpeedChangeListener != null) onSpeedChangeListener.onChange(speed);
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setGraphicEnable(boolean enable) {
        this.graphic = enable;
    }

    public boolean isGraphicEnable() {
        return graphic;
    }

    public int getSynchronizeOption() {
        return synchronizeOption;
    }

    public void setSynchronizeOption(int synchronizeOption) {
        this.synchronizeOption = synchronizeOption;
    }

    public void setOnPauseListener(OnPauseListener listener) {
        this.onPauseListener = listener;
    }
    
    public void setOnSpeedChangeListener(OnSpeedChangeListener listener){
        this.onSpeedChangeListener = listener;
    }
    
}
