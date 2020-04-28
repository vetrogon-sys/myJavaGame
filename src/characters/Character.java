package characters;

import mainFiles.Direction;
import mainFiles.gameConventions.GameFraction;
import mainFiles.MyMain;

import java.awt.*;

import static mainFiles.Direction.*;

public abstract class Character {
    private static final int SIZE_DIF = 12;
    public static GameFraction fraction;

    public MyMain game;

    public int dirX;
    public int dirY;
    public int speed;

    public float hp;

    public Direction direction = RIGHT_DOWN;
    public State state = State.ALIVE;
    public Activity activity = Activity.STAND;

    public Image currentFrame;
    public CharacterSprite sprites;

    public int spriteWidth;
    public int spriteHeight;
    public float time = 0;


    public Character(MyMain game) {
        this.game = game;

    }

    public void move() throws NullPointerException {
        Direction blockedDir = blockedDir();

        if (fraction == game.player.fraction
                && game.player.party.contains(this)) {
            isOnPlace();
            game.player.changeDirection(this);
        }

        if (direction != blockedDir
                && state == State.ALIVE
                && activity == Activity.WALK) {
            if (time >= 4.8) {
                time = 0;
            }
            time += 0.198f;

            currentFrame = sprites.characterSprites.get(direction).get(activity)[(int) time];
        }
        if (state == State.ALIVE
                && activity == Activity.ATTACK) {
            if (time <= 3.8) {
                currentFrame = sprites.characterSprites.get(direction).get(activity)[(int) time];
                time += 0.25f;
            } else {
                activity = Activity.STAND;
                time = 0;
            }

        }
        if (state == State.DEAD) {
            if (activity == Activity.DIE) {
                time += 0.25f;
                if (time >= 1.75) {
                    activity = Activity.STAND;
                    time = 2;
                }
                currentFrame = sprites.characterSprites.get(direction).get(activity)[(int) time];
            }
        }
        if (state == State.ALIVE && activity == Activity.STAND) {
            currentFrame = sprites.characterSprites.get(direction).get(activity)[0];
        }

        if (state == State.ALIVE
                && activity == Activity.WALK
                && direction != blockedDir) {
            switch (direction) {
                case UP:
                    dirY -= speed;
                    break;
                case RIGHT_UP:
                    dirX += (int) (speed / 1.5);
                    dirY -= (int) (speed / 1.5);
                    break;
                case RIGHT:
                    dirX += speed;
                    break;
                case RIGHT_DOWN:
                    dirX += (int) (speed / 1.5);
                    dirY += (int) (speed / 1.5);
                    break;
                case DOWN:
                    dirY += speed;
                    break;
                case LEFT_DOWN:
                    dirX -= (int) (speed / 1.5);
                    dirY += (int) (speed / 1.5);
                    break;
                case LEFT:
                    dirX -= speed;
                    break;
                case LEFT_UP:
                    dirX -= (int) (speed / 1.5);
                    dirY -= (int) (speed / 1.5);
                    break;
                default:
                    break;
            }
        }
    }

    public Direction blockedDir() {
        for (Character character : game.characters) {
            if (blockCheck(character) != Direction.NONE) {
                return blockCheck(character);
            }
        }
        return Direction.NONE;
    }

    private Direction blockCheck(Character obj) {
        if (state != State.DEAD && obj != this) {
            if (dirX + spriteWidth + SIZE_DIF >= obj.dirX
                    && dirX < obj.dirX
                    && (dirY >= obj.dirY && dirY <= obj.dirY + obj.spriteHeight
                    || dirY + spriteHeight >= obj.dirY && dirY + spriteHeight <= obj.dirY + obj.spriteHeight
                    || dirY < obj.dirY && dirY + spriteHeight > obj.dirY + obj.spriteHeight)) {
                return Direction.RIGHT;
            }
            if (dirX <= obj.dirX + obj.spriteWidth + SIZE_DIF
                    && dirX + spriteWidth > obj.dirX
                    && (dirY >= obj.dirY && dirY <= obj.dirY + obj.spriteHeight
                    || dirY + spriteHeight >= obj.dirY && dirY + spriteHeight <= obj.dirY + obj.spriteHeight
                    || dirY < obj.dirY && dirY + spriteHeight > obj.dirY + obj.spriteHeight)) {
                return Direction.LEFT;
            }
            if (dirY <= obj.dirY + obj.spriteHeight + SIZE_DIF * 2
                    && dirY + spriteHeight > obj.dirY + obj.spriteHeight
                    && (dirX >= obj.dirX && dirX <= obj.dirX + obj.spriteWidth
                    || dirX + spriteWidth >= obj.dirX && dirX + spriteWidth <= obj.dirX + obj.spriteWidth
                    || dirX < obj.dirX && dirX + spriteWidth > obj.dirX + obj.spriteWidth)) {
                return Direction.UP;
            }
            if (dirY + spriteHeight + SIZE_DIF * 2 >= obj.dirY
                    && dirY - spriteHeight < obj.dirY
                    && (dirX >= obj.dirX && dirX <= obj.dirX + obj.spriteWidth
                    || dirX + spriteWidth >= obj.dirX && dirX + spriteWidth <= obj.dirX + obj.spriteWidth
                    || dirX < obj.dirX && dirX + spriteWidth > obj.dirX + obj.spriteWidth)) {
                return Direction.DOWN;
            }
            if(dirX + spriteWidth >= obj.dirX && dirY > obj.dirY && dirY < obj.dirY + obj.spriteHeight) {
                return RIGHT_UP;
            }
            if(dirX + spriteWidth >= obj.dirX && dirY + spriteWidth > obj.dirY && dirY + spriteWidth < obj.dirY + obj.spriteHeight) {
                return RIGHT_DOWN;
            }
            if(dirX <= obj.dirX && dirY > obj.dirY && dirY < obj.dirY + obj.spriteHeight) {
                return LEFT_UP;
            }
            if(dirX <= obj.dirX && dirY + spriteWidth > obj.dirY && dirY + spriteWidth < obj.dirY + obj.spriteHeight) {
                return LEFT_DOWN;
            }
            return Direction.NONE;
        }
        return Direction.NONE;
    }

    public boolean isOnPlace() {
        if (dirX < game.player.x && dirX + spriteWidth > game.player.x
                && dirY < game.player.y && dirY + spriteHeight > game.player.y) {
            activity = Activity.STAND;
            return true;
        } else {
            activity = Activity.WALK;
            return false;
        }
    }

}