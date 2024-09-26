package ac.rino.rinoac.utils.collisions;

import ac.rino.rinoac.utils.collisions.datatypes.SimpleCollisionBox;

public interface AxisSelect {
    SimpleCollisionBox modify(SimpleCollisionBox box);
}