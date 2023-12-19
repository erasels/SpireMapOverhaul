package spireMapOverhaul.ui;

import basemod.ClickableUIElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import spireMapOverhaul.util.ImageHelper;

import java.util.function.Supplier;

public class GenericButton extends ClickableUIElement {
    protected static final Color BASE_COL = Color.LIGHT_GRAY, HOVER_COL = Color.WHITE;
    protected String boxTitle, boxDesc;
    protected Runnable exec;
    protected Supplier<Boolean> condition;
    protected Color renderCol = BASE_COL;
    protected boolean draggable = true, isBeingDragged = false;
    protected float initialDragOffsetX,  initialDragOffsetY;

    //x and y should be multiplied by Settings.scale
    public GenericButton(Texture tex, float x, float y) {
        super(tex);
        setX(x);
        setY(y);
    }

    public GenericButton setHoverTip(String title, String description) {
        boxTitle = title;
        boxDesc = description;
        return this;
    }

    public GenericButton setClickFunc(Runnable runnable) {
        exec = runnable;
        return this;
    }

    public GenericButton setClickableCond(Supplier<Boolean> condition) {
        this.condition = condition;
        return this;
    }

    public GenericButton setDraggable(boolean b) {
        draggable = b;
        return this;
    }

    @Override
    protected void onClick() {
        if (exec != null)
            exec.run();
    }

    @Override
    public boolean isClickable() {
        return super.isClickable() && condition.get();
    }

    @Override
    protected void onHover() {
        if(boxTitle != null)
            ImageHelper.tipBoxAtMousePos(boxTitle, boxDesc);
        renderCol = HOVER_COL;
    }

    @Override
    protected void onUnhover() {
        renderCol = BASE_COL;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb, renderCol);
    }

    public void update() {
        super.update();

        if (this.hitbox.hovered && InputHelper.justClickedRight && this.draggable && !isBeingDragged) {
            isBeingDragged = true;
            initialDragOffsetX = InputHelper.mX - x;
            initialDragOffsetY = InputHelper.mY - y;
        }

        if (isBeingDragged) {
            // Use the initial offset to adjust the button's position
            setX(InputHelper.mX - initialDragOffsetX);
            setY(InputHelper.mY - initialDragOffsetY);

            if (InputHelper.justReleasedClickRight) {
                isBeingDragged = false;
                //TODO: Save this position to config?
            }
        }
    }
}