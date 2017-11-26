package com.pi.common.utils.enums;

import com.pi.common.utils.exceptions.PiRuntimeException;
import com.pi.common.utils.i18n.ValidationCode;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;

import java.beans.PropertyEditorSupport;

public class EnumEntityPropertyEditor<T extends EnumEntity<?>> extends PropertyEditorSupport {

    private Class<T> enumEntityClass;

    private Class<?> valueType;

    private ConversionService conversionService;

    public EnumEntityPropertyEditor(Class<T> enumEntityClass, ConversionService conversionService) {
        super();
        this.enumEntityClass = enumEntityClass;
        this.valueType = EnumEntityUtils.getEnumEntityValueType(enumEntityClass);
        this.conversionService = conversionService == null ? new DefaultConversionService() : conversionService;
    }

    @Override
    public String getAsText() {
        @SuppressWarnings("unchecked")
        T enumEntity = (T) getValue();
        return conversionService.convert(enumEntity.getValue(), String.class);
    }

    @Override
    public void setAsText(String text)
            throws IllegalArgumentException {
        Object value = conversionService.convert(text, valueType);
        T enumEntity = EnumEntityFactory.getUnchecked(enumEntityClass, value);
        if (enumEntity == null) {
            throw new PiRuntimeException(HttpStatus.BAD_REQUEST, ValidationCode.FATAL_ERROR);
        }
        setValue(enumEntity);
    }
}
