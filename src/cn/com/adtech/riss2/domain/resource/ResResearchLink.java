package cn.com.adtech.riss2.domain.resource;

import java.io.Serializable;
import java.util.Objects;

/** A paper/monograph (researchId) about an explicitly selected resource (resourceId). */
public class ResResearchLink implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long researchId;
    private Long resourceId;
    public ResResearchLink() { }
    public ResResearchLink(Long researchId, Long resourceId) { this.researchId=researchId; this.resourceId=resourceId; }
    public Long getResearchId() { return researchId; }
    public void setResearchId(Long value) { researchId=value; }
    public Long getResourceId() { return resourceId; }
    public void setResourceId(Long value) { resourceId=value; }
    @Override public boolean equals(Object value) {
        if (!(value instanceof ResResearchLink)) return false;
        ResResearchLink other=(ResResearchLink)value;
        return Objects.equals(researchId,other.researchId) && Objects.equals(resourceId,other.resourceId);
    }
    @Override public int hashCode() { return Objects.hash(researchId,resourceId); }
}
