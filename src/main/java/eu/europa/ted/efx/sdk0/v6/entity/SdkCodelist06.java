package eu.europa.ted.efx.sdk0.v6.entity;

import java.util.List;
import eu.europa.ted.eforms.sdk.entity.SdkCodelist;
import eu.europa.ted.eforms.sdk.selector.component.VersionDependentComponent;
import eu.europa.ted.eforms.sdk.selector.component.VersionDependentComponentType;

/**
 * Representation of an SdkCodelist for usage in the symbols map.
 *
 * @author rouschr
 */
@VersionDependentComponent(versions = {"0.6"}, componentType = VersionDependentComponentType.CODELIST)
public class SdkCodelist06 extends SdkCodelist {

    public SdkCodelist06(String codelistId, String codelistVersion, List<String> codes) {
        super(codelistId, codelistVersion, codes);
    }
}
