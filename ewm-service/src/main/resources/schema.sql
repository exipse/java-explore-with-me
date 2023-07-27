DROP TABLE IF EXISTS REQUEST;
DROP TABLE IF EXISTS COMPILATION_EVENT;
DROP TABLE IF EXISTS COMPILATION;
DROP TABLE IF EXISTS EVENT;
DROP TABLE IF EXISTS CATEGORY;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS LOCATION;


CREATE TABLE USERS
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(250)  NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE CATEGORY
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50) UNIQUE NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT category_name UNIQUE (name)
);

CREATE TABLE LOCATION
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat REAL NOT NULL,
    lon REAL NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)

);

CREATE TABLE COMPILATION
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned bool NOT NULL,
    title  VARCHAR(50) NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
    );

CREATE TABLE EVENT
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000) NOT NULL,
    category_id        BIGINT NOT NULL REFERENCES CATEGORY (id),
    created_on         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description        VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id       BIGINT NOT NULL REFERENCES USERS (id),
    location_id        BIGINT NOT NULL REFERENCES LOCATION (id),
    paid               bool NOT NULL,
    participant_limit  BIGINT NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderator  bool NOT NULL,
    state              VARCHAR NOT NULL,
    title              VARCHAR(120) NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id)
);

CREATE TABLE REQUEST
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP NOT NULL,
    event_id     BIGINT NOT NULL REFERENCES EVENT (id),
    requester_id BIGINT NOT NULL REFERENCES USERS (id),
    status       VARCHAR NOT NULL,

    CONSTRAINT pk_request PRIMARY KEY (id),
    UNIQUE (event_id, requester_id)
);

CREATE TABLE COMPILATION_EVENT
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    compilation_id BIGINT NOT NULL REFERENCES COMPILATION (id),
    event_id       BIGINT NOT NULL REFERENCES EVENT (id),

    CONSTRAINT pk_compevnt PRIMARY KEY (id)
);


