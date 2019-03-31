/*
 * Copyright sablintolya@gmai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ma1uta.saimaa.module.activitypub.model.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.ma1uta.saimaa.module.activitypub.helpers.ObjectDeserializer;
import io.github.ma1uta.saimaa.module.activitypub.helpers.UrlDeserializer;
import io.github.ma1uta.saimaa.module.activitypub.model.object.Document;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Describes an object of any kind. The Object type serves as the base type for most of the other kinds of objects defined
 * in the Activity Vocabulary, including other Core types such as Activity, IntransitiveActivity, Collection and OrderedCollection.
 * <br>
 * <a href="https://www.w3.org/TR/activitystreams-vocabulary/#dfn-object">Formal definition.</a>
 */
public class Object extends Contextable {

    /**
     * Type.
     */
    public static final String TYPE = "Object";

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object attachment;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object attributedTo;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object audience;

    private String content;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object context;

    private String name;

    private Instant endTime;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object generator;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object icon;

    private Document image;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object inReplyTo;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object location;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object preview;

    private Instant published;

    private Collection replies;

    private Instant startTime;

    private String summary;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private List<java.lang.Object> tag;

    private Instant updated;

    @JsonDeserialize(using = UrlDeserializer.class)
    private java.lang.Object url;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private List<java.lang.Object> to;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private List<java.lang.Object> bto;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private List<java.lang.Object> cc;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private List<java.lang.Object> bcc;

    private String mediaType;

    private Duration duration;

    @Override
    public String getType() {
        return TYPE;
    }

    public java.lang.Object getAttachment() {
        return attachment;
    }

    public void setAttachment(java.lang.Object attachment) {
        this.attachment = attachment;
    }

    public java.lang.Object getAttributedTo() {
        return attributedTo;
    }

    public void setAttributedTo(java.lang.Object attributedTo) {
        this.attributedTo = attributedTo;
    }

    public java.lang.Object getAudience() {
        return audience;
    }

    public void setAudience(java.lang.Object audience) {
        this.audience = audience;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public java.lang.Object getContext() {
        return context;
    }

    public void setContext(java.lang.Object context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public java.lang.Object getGenerator() {
        return generator;
    }

    public void setGenerator(java.lang.Object generator) {
        this.generator = generator;
    }

    public java.lang.Object getIcon() {
        return icon;
    }

    public void setIcon(java.lang.Object icon) {
        this.icon = icon;
    }

    public Document getImage() {
        return image;
    }

    public void setImage(Document image) {
        this.image = image;
    }

    public java.lang.Object getInReplyTo() {
        return inReplyTo;
    }

    public void setInReplyTo(java.lang.Object inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    public java.lang.Object getLocation() {
        return location;
    }

    public void setLocation(java.lang.Object location) {
        this.location = location;
    }

    public java.lang.Object getPreview() {
        return preview;
    }

    public void setPreview(java.lang.Object preview) {
        this.preview = preview;
    }

    public Instant getPublished() {
        return published;
    }

    public void setPublished(Instant published) {
        this.published = published;
    }

    public Collection getReplies() {
        return replies;
    }

    public void setReplies(Collection replies) {
        this.replies = replies;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<java.lang.Object> getTag() {
        return tag;
    }

    public void setTag(List<java.lang.Object> tag) {
        this.tag = tag;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public java.lang.Object getUrl() {
        return url;
    }

    public void setUrl(java.lang.Object url) {
        this.url = url;
    }

    public List<java.lang.Object> getTo() {
        return to;
    }

    public void setTo(List<java.lang.Object> to) {
        this.to = to;
    }

    public List<java.lang.Object> getBto() {
        return bto;
    }

    public void setBto(List<java.lang.Object> bto) {
        this.bto = bto;
    }

    public List<java.lang.Object> getCc() {
        return cc;
    }

    public void setCc(List<java.lang.Object> cc) {
        this.cc = cc;
    }

    public List<java.lang.Object> getBcc() {
        return bcc;
    }

    public void setBcc(List<java.lang.Object> bcc) {
        this.bcc = bcc;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
