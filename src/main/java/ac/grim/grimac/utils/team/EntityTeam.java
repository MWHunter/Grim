package ac.grim.grimac.utils.team;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.protocol.player.UserProfile;
import ac.grim.grimac.shaded.com.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class EntityTeam {

    private final GrimPlayer player;
    public final String name;
    public final Set<String> entries = new HashSet<>();
    @Getter private WrapperPlayServerTeams.CollisionRule collisionRule;

    public EntityTeam(GrimPlayer player, String name) {
        this.player = player;
        this.name = name;
    }

    public void update(WrapperPlayServerTeams teams) {
        teams.getTeamInfo().ifPresent(info -> this.collisionRule = info.getCollisionRule());

        final WrapperPlayServerTeams.TeamMode mode = teams.getTeamMode();
        if (mode == WrapperPlayServerTeams.TeamMode.ADD_ENTITIES || mode == WrapperPlayServerTeams.TeamMode.CREATE) {
            final TeamHandler teamHandler = player.checkManager.getPacketCheck(TeamHandler.class);
            for (String teamsPlayer : teams.getPlayers()) {
                if (teamsPlayer.equals(player.user.getName())) {
                    player.teamName = name;
                    continue;
                }

                boolean flag = false;
                for (UserProfile profile : player.compensatedEntities.profiles.values()) {
                    if (profile.getName() != null && profile.getName().equals(teamsPlayer)) {
                        teamHandler.addEntityToTeam(profile.getUUID().toString(), this);
                        flag = true;
                    }
                }

                if (flag) continue;

                teamHandler.addEntityToTeam(teamsPlayer, this);
            }
        } else if (mode == WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES) {
            for (String teamsPlayer : teams.getPlayers()) {
                if (teamsPlayer.equals(player.user.getName())) {
                    player.teamName = null;
                    continue;
                }
                entries.remove(teamsPlayer);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof EntityTeam t && Objects.equals(name, t.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
