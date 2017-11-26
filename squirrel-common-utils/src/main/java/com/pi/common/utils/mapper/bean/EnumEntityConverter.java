package com.pi.common.utils.mapper.bean;

import com.pi.common.utils.enums.EnumEntity;
import com.pi.common.utils.enums.EnumEntityFactory;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;

public class EnumEntityConverter extends CustomConverter<Object, Object> {

    @Override
    public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
        return EnumEntity.class.isAssignableFrom(sourceType.getRawType())
                && !EnumEntity.class.isAssignableFrom(destinationType.getRawType())
                || !EnumEntity.class.isAssignableFrom(sourceType.getRawType())
                        && EnumEntity.class.isAssignableFrom(destinationType.getRawType());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Object source, Type<? extends Object> destinationType) {
        if (EnumEntity.class.isAssignableFrom(destinationType.getRawType())) {
            return EnumEntityFactory.getUnchecked((Class<EnumEntity<?>>) destinationType.getRawType(), source);
        } else {
            return ((EnumEntity<?>) source).getValue();
        }
    }

}
