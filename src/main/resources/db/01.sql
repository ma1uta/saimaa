-- common apservice
CREATE TABLE "app_user"
(
    "localpart" TEXT PRIMARY KEY
);

CREATE TABLE "transaction"
(
    "id"        TEXT PRIMARY KEY,
    "started"   TIMESTAMP WITH TIME ZONE,
    "processed" TIMESTAMP WITH TIME ZONE
);

-- activity pub
CREATE TABLE "ap_user"
(
    "room_id"  TEXT PRIMARY KEY,
    "mxid"     TEXT,
    "username" TEXT,
    "name"     TEXT,
    "group"    BOOLEAN,
    "icon"     TEXT
);

CREATE TABLE "ap_followers"
(
    "room_id"  TEXT REFERENCES "ap_user" ("room_id"),
    "follower" TEXT,
    "added"    TIMESTAMP WITH TIME ZONE
);

CREATE TABLE "ap_following"
(
    "room_id"   TEXT REFERENCES "ap_user" ("room_id"),
    "following" TEXT,
    "added"     TIMESTAMP WITH TIME ZONE
);

-- xmpp
CREATE TABLE "multi_user_room"
(
    "room_alias" TEXT PRIMARY KEY,
    "room_id"    TEXT,
    "conference" TEXT
);

CREATE TABLE "direct_room"
(
    "room_id"     TEXT,
    "matrix_user" TEXT,
    "xmpp_user"   TEXT,
    "matrix_subs" BOOLEAN,
    "xmpp_subs"   BOOLEAN,
    PRIMARY KEY ("matrix_user", "xmpp_user")
);

CREATE TABLE "inviters"
(
    "room_id" VARCHAR(255) PRIMARY KEY,
    "user_id" VARCHAR(255) NOT NULL
);
