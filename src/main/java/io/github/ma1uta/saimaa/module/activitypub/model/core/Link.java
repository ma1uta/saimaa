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

/**
 * A Link is an indirect, qualified reference to a resource identified by a URL.
 * The fundamental model for links is established by [ RFC5988]. Many of the properties defined by the Activity Vocabulary allow values
 * that are either instances of Object or Link. When a Link is used, it establishes a qualified relation connecting the subject
 * (the containing object) to the resource identified by the href.
 * Properties of the Link are properties of the reference as opposed to properties of the resource.
 *
 * <a href="https://www.w3.org/TR/activitystreams-vocabulary/#dfn-link">Formal definition.</a>
 */
public class Link extends Contextable {

    /**
     * Type.
     */
    public static final String TYPE = "Link";

    private String href;

    private String rel;

    private String mediaType;

    private String name;

    private String hreflang;

    private Long height;

    private Long width;

    @JsonDeserialize(using = ObjectDeserializer.class)
    private java.lang.Object preview;

    @Override
    public String getType() {
        return TYPE;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHreflang() {
        return hreflang;
    }

    public void setHreflang(String hreflang) {
        this.hreflang = hreflang;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public java.lang.Object getPreview() {
        return preview;
    }

    public void setPreview(java.lang.Object preview) {
        this.preview = preview;
    }
}
