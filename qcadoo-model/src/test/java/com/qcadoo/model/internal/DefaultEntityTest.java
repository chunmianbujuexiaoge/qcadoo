/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo Framework
 * Version: 1.1.6
 *
 * This file is part of Qcadoo.
 *
 * Qcadoo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */
package com.qcadoo.model.internal;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.Entity;
import com.qcadoo.model.api.EntityList;
import com.qcadoo.model.api.FieldDefinition;
import com.qcadoo.model.api.types.BelongsToType;
import com.qcadoo.model.api.types.FieldType;

public class DefaultEntityTest {

    private DefaultEntity defaultEntity;

    private DataDefinition dataDefinition;

    private FieldDefinition belongsToFieldDefinition;

    private DataDefinition belongsToFieldDataDefinition;

    private static final String BELONGS_TO_FIELD_NAME = "belongsToField";

    private static final String STRING_FIELD_NAME = "stringField";

    private static final String BOOLEAN_FIELD_NAME = "booleanField";

    @Before
    public final void init() {
        belongsToFieldDefinition = mock(FieldDefinition.class);
        BelongsToType belongsToType = mock(BelongsToType.class);
        when(belongsToFieldDefinition.getType()).thenReturn(belongsToType);
        belongsToFieldDataDefinition = mock(DataDefinition.class);
        when(belongsToFieldDefinition.getDataDefinition()).thenReturn(belongsToFieldDataDefinition);
        FieldDefinition stringFieldDefinition = mock(FieldDefinition.class);
        when(stringFieldDefinition.isPersistent()).thenReturn(false);

        dataDefinition = mock(DataDefinition.class);
        when(dataDefinition.getField(BELONGS_TO_FIELD_NAME)).thenReturn(belongsToFieldDefinition);
        when(dataDefinition.getField(STRING_FIELD_NAME)).thenReturn(stringFieldDefinition);
        FieldDefinition booleanFieldDefinition = mock(FieldDefinition.class);
        when(dataDefinition.getField(BOOLEAN_FIELD_NAME)).thenReturn(booleanFieldDefinition);

        defaultEntity = new DefaultEntity(dataDefinition);
    }

    @Test
    public final void shouldGetBelongsToFieldReturnEntity() throws Exception {
        // given
        Entity belongsToEntity = mock(Entity.class);
        defaultEntity.setField(BELONGS_TO_FIELD_NAME, belongsToEntity);

        // when
        Entity returnedEntity = defaultEntity.getBelongsToField(BELONGS_TO_FIELD_NAME);

        // then
        assertEquals(belongsToEntity, returnedEntity);
    }

    @Test
    public final void shouldGetBelongsToFieldReturnProxyEntity() throws Exception {
        // given
        Long belongsToEntityId = 1L;
        defaultEntity.setField(BELONGS_TO_FIELD_NAME, belongsToEntityId);

        // when
        Entity returnedEntity = defaultEntity.getBelongsToField(BELONGS_TO_FIELD_NAME);

        // then
        assertEquals(belongsToEntityId, returnedEntity.getId());
    }

    @Test
    public final void shouldGetBelongsToFieldReturnProxyEntityUsingIntegerValue() throws Exception {
        // given
        Integer belongsToEntityId = 1;
        defaultEntity.setField(BELONGS_TO_FIELD_NAME, belongsToEntityId);

        // when
        Entity returnedEntity = defaultEntity.getBelongsToField(BELONGS_TO_FIELD_NAME);

        // then
        assertTrue(returnedEntity.getId() instanceof Long);
        assertFalse(belongsToEntityId.equals(returnedEntity.getId()));
        assertEquals(belongsToEntityId.longValue(), returnedEntity.getId().longValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void shouldGetBelongsToFieldThrowIllegalArgumentExceptionIfFieldIsNotABelongsToType() throws Exception {
        // given
        FieldType wrongFieldType = mock(FieldType.class);
        when(belongsToFieldDefinition.getType()).thenReturn(wrongFieldType);
        defaultEntity.setField(BELONGS_TO_FIELD_NAME, "some wrong type value");

        // when
        defaultEntity.getBelongsToField(BELONGS_TO_FIELD_NAME);
    }

    @Test
    public final void shouldGetBelongsToFieldReturnNullIfFieldIsEmpty() throws Exception {
        // when
        Entity returnedEntity = defaultEntity.getBelongsToField(BELONGS_TO_FIELD_NAME);

        // then
        assertNull(returnedEntity);
    }

    @Test
    public final void shouldGetDataDefinitionReturnDataDefinition() throws Exception {
        // when
        DataDefinition returnedDataDefinition = defaultEntity.getDataDefinition();

        // then
        assertEquals(dataDefinition, returnedDataDefinition);
    }

    @Test
    public final void shouldCopyReturnCopyOfEntity() throws Exception {
        // given
        Entity belongsToEntity = mock(Entity.class);
        when(belongsToEntity.getDataDefinition()).thenReturn(belongsToFieldDataDefinition);
        when(belongsToEntity.copy()).thenReturn(belongsToEntity);

        defaultEntity.setId(1L);
        defaultEntity.setField(BELONGS_TO_FIELD_NAME, belongsToEntity);
        defaultEntity.setField(STRING_FIELD_NAME, "some string value");

        // when
        Entity returnedCopy = defaultEntity.copy();

        // then
        assertEquals(defaultEntity, returnedCopy);
        assertEquals(defaultEntity.hashCode(), returnedCopy.hashCode());
        assertNotSame(defaultEntity, returnedCopy);
    }

    @Test
    public final void shouldGetBooleanValueReturnTrueIfFieldValueIsTrue() throws Exception {
        // given
        defaultEntity.setField(BOOLEAN_FIELD_NAME, true);

        // when
        boolean returnedValue = defaultEntity.getBooleanField(BOOLEAN_FIELD_NAME);

        // then
        assertTrue(returnedValue);
    }

    @Test
    public final void shouldGetBooleanValueReturnFalseIfFieldValueIsFalse() throws Exception {
        // given
        defaultEntity.setField(BOOLEAN_FIELD_NAME, false);

        // when
        boolean returnedValue = defaultEntity.getBooleanField(BOOLEAN_FIELD_NAME);

        // then
        assertFalse(returnedValue);
    }

    @Test
    public final void shouldGetBooleanValueReturnTrueIfFieldValueIsStringTrue() throws Exception {
        // given
        defaultEntity.setField(BOOLEAN_FIELD_NAME, "true");

        // when
        boolean returnedValue = defaultEntity.getBooleanField(BOOLEAN_FIELD_NAME);

        // then
        assertTrue(returnedValue);
    }

    @Test
    public final void shouldGetBooleanValueReturnTrueIfFieldValueIsStringUpperCaseTrue() throws Exception {
        // given
        defaultEntity.setField(BOOLEAN_FIELD_NAME, "TRUE");

        // when
        boolean returnedValue = defaultEntity.getBooleanField(BOOLEAN_FIELD_NAME);

        // then
        assertTrue(returnedValue);
    }

    @Test
    public final void shouldGetBooleanValueReturnFalseIfFieldValueIsStringFalse() throws Exception {
        // given
        defaultEntity.setField(BOOLEAN_FIELD_NAME, "false");

        // when
        boolean returnedValue = defaultEntity.getBooleanField(BOOLEAN_FIELD_NAME);

        // then
        assertFalse(returnedValue);
    }

    @Test
    public final void shouldGetBooleanValueReturnFalseIfFieldValueIsStringOtherThanTrueOrFalse() throws Exception {
        // given
        defaultEntity.setField(BOOLEAN_FIELD_NAME, "some other string");

        // when
        boolean returnedValue = defaultEntity.getBooleanField(BOOLEAN_FIELD_NAME);

        // then
        assertFalse(returnedValue);
    }

    @Test
    public final void shouldGetBooleanValueReturnFalseIfFieldValueIsNotAppropriateType() throws Exception {
        // given
        defaultEntity.setField(BOOLEAN_FIELD_NAME, 1);

        // when
        boolean returnedValue = defaultEntity.getBooleanField(BOOLEAN_FIELD_NAME);

        // then
        assertFalse(returnedValue);
    }

    @Test
    public final void shouldGetBooleanValueReturnFalseIfFieldValueIsNull() throws Exception {
        // given
        defaultEntity.setField(BOOLEAN_FIELD_NAME, null);

        // when
        boolean returnedValue = defaultEntity.getBooleanField(BOOLEAN_FIELD_NAME);

        // then
        assertFalse(returnedValue);
    }

    @Test
    public final void shouldReturnBigDecimalValue() throws Exception {
        // given
        BigDecimal decimal = BigDecimal.ZERO;
        final String decimalFieldName = "decimalField";
        defaultEntity.setField(decimalFieldName, decimal);

        // when
        BigDecimal result = defaultEntity.getDecimalField(decimalFieldName);

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(decimal, result);
    }

    @Test
    public final void shouldReturnNullIfDecimalFieldIsNull() throws Exception {
        // given
        final String decimalFieldName = "decimalField";
        defaultEntity.setField(decimalFieldName, null);

        // when
        BigDecimal result = defaultEntity.getDecimalField(decimalFieldName);

        // then
        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void shouldThrowExceptionIfDecimalFieldTypeIsNotSupported() throws Exception {
        // given
        final String decimalFieldName = "decimalField";
        defaultEntity.setField(decimalFieldName, "qcadoo rlz!");

        // when
        defaultEntity.getDecimalField(decimalFieldName);
    }

    @Test
    public final void shouldReturnDetachedEntityListImpl() throws Exception {
        // given
        final String hasManyFieldName = "hasManyDetachedField";
        List<Entity> listOfMockEntities = getListOfMockEntities();
        defaultEntity.setField(hasManyFieldName, listOfMockEntities);

        // when
        EntityList resultList = defaultEntity.getHasManyField(hasManyFieldName);

        // then
        Assert.assertTrue(resultList instanceof DetachedEntityListImpl);
        Assert.assertEquals(listOfMockEntities.size(), resultList.size());
        Assert.assertArrayEquals(listOfMockEntities.toArray(), resultList.toArray());
    }

    @Test
    public final void shouldReturnExistingEntityList() throws Exception {
        // given
        final String hasManyFieldName = "hasManyDetachedField";
        EntityList entityList = mock(EntityList.class);
        defaultEntity.setField(hasManyFieldName, entityList);

        // when
        EntityList resultList = defaultEntity.getHasManyField(hasManyFieldName);

        // then
        Assert.assertTrue(resultList instanceof EntityList);
        Assert.assertEquals(entityList, resultList);
    }

    @Test
    public final void shouldReturnEmptyDetachedEntityListIfFieldIsNull() throws Exception {
        // given
        final String hasManyFieldName = "hasManyDetachedField";
        defaultEntity.setField(hasManyFieldName, null);

        // when
        EntityList resultList = defaultEntity.getHasManyField(hasManyFieldName);

        // then
        Assert.assertNotNull(resultList);
        Assert.assertTrue(resultList instanceof DetachedEntityListImpl);
        Assert.assertTrue(resultList.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void shouldThrowExceptionIfHasManyFieldHaveUnsupportedType() throws Exception {
        // given
        final String hasManyFieldName = "hasManyDetachedField";
        defaultEntity.setField(hasManyFieldName, "usupported value type (String)");

        // when
        defaultEntity.getHasManyField(hasManyFieldName);
    }

    private List<Entity> getListOfMockEntities() {
        Entity e1 = mock(Entity.class);
        Entity e2 = mock(Entity.class);
        Entity e3 = mock(Entity.class);

        when(e1.getId()).thenReturn(1L);
        when(e2.getId()).thenReturn(2L);
        when(e3.getId()).thenReturn(3L);

        return Lists.newArrayList(e1, e2, e3);
    }
}
