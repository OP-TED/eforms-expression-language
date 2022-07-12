package eu.europa.ted.efx.sdk0.v6.model;

import com.fasterxml.jackson.databind.JsonNode;
import eu.europa.ted.eforms.sdk.annotation.SdkComponent;
import eu.europa.ted.eforms.sdk.component.SdkComponentTypeEnum;
import eu.europa.ted.efx.model.SdkNode;

/**
 * A node is something like a section. Nodes can be parents of other nodes or parents of fields.
 */
@SdkComponent(versions = {"0.6"}, componentType = SdkComponentTypeEnum.NODE)
public class SdkNode06 extends SdkNode {

    public SdkNode06(String id, String parentId, String xpathAbsolute, String xpathRelative,
            boolean repeatable) {
        super(id, parentId, xpathAbsolute, xpathRelative, repeatable);
    }

    public SdkNode06(JsonNode node) {
        super(node);
    }
}
