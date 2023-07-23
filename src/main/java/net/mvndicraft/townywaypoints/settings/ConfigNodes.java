package net.mvndicraft.townywaypoints.settings;

public enum ConfigNodes {
    VERSION(
            "version",
            "",
            "# This is the current version. Please do not edit."),
    LANGUAGE("language",
            "en_US.yml",
            "# The language file you wish to use."),
    WAYPOINTS("waypoints","",""),
    WAYPOINTS_COOLDOWNS(
            "waypoints.cooldowns",
            "",
            "",
            "",
            "############################################################",
            "# +------------------------------------------------------+ #",
            "# |                   Cooldown settings                  | #",
            "# +------------------------------------------------------+ #",
            "############################################################",
    ""),
    WAYPOINTS_COOLDOWNS_ENABLED(
            "waypoints.cooldowns.enabled",
            "true",
            "",
            "# If true, cooldowns are enabled.",
            "# if false, cooldowns are disabled."),
    WAYPOINTS_COOLDOWNS_MAX_COOLDOWN(
            "waypoints.cooldowns.max_cooldown",
            "30.0",
            "",
            "# The maximum amount of time a players teleport cooldown can be.");

    private final String Root;
    private final String Default;
    private final String[] comments;

    ConfigNodes(String root, String def, String... comments) {

        this.Root = root;
        this.Default = def;
        this.comments = comments;
    }

    /**
     * Retrieves the root for a config option
     *
     * @return The root for a config option
     */
    public String getRoot() {

        return Root;
    }

    /**
     * Retrieves the default value for a config path
     *
     * @return The default value for a config path
     */
    public String getDefault() {

        return Default;
    }

    /**
     * Retrieves the comment for a config path
     *
     * @return The comments for a config path
     */
    public String[] getComments() {

        if (comments != null) {
            return comments;
        }

        String[] comments = new String[1];
        comments[0] = "";
        return comments;
    }
}
