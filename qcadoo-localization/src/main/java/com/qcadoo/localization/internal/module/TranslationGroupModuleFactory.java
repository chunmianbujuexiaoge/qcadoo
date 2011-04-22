package com.qcadoo.localization.internal.module;

import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.qcadoo.localization.internal.InternalTranslationService;
import com.qcadoo.plugin.api.ModuleFactory;

public class TranslationGroupModuleFactory extends ModuleFactory<TranslationGroupModule> {

    @Autowired
    private InternalTranslationService translationService;

    @Override
    public TranslationGroupModule parse(final String pluginIdentifier, final Element element) {
        String prefix = element.getAttributeValue("prefix");

        if (prefix == null) {
            throw new IllegalStateException("Missing prefix attribute of localization module");
        }

        String name = element.getAttributeValue("name");

        return new TranslationGroupModule(translationService, prefix, name);
    }

    @Override
    public String getIdentifier() {
        return "translation-group";
    }

}
