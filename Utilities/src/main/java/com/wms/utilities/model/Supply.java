package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Supply {
    private int id;
    private Integer warehouseId;
    private Integer supplierId;
    private Integer materialId;
    private BigDecimal defaultEntryAmount;
    private String defaultEntryUnit;
    private BigDecimal defaultEntryUnitAmount;
    private BigDecimal defaultInspectionAmount;
    private String defaultInspectionUnit;
    private BigDecimal defaultInspectionUnitAmount;
    private BigDecimal defaultDeliveryAmount;
    private String defaultDeliveryUnit;
    private BigDecimal defaultDeliveryUnitAmount;
    private BigDecimal validPeriod;
    private String photoIndex;
    private String containerNo;
    private String factory;
    private String workPosition;
    private String supplierType;
    private String type;
    private String size;
    private String groupPrincipal;
    private String singleBoxPhotoIndex;
    private String singleBoxPackagingBoxType;
    private BigDecimal singleBoxLength;
    private BigDecimal singleBoxWidth;
    private BigDecimal singleBoxHeight;
    private BigDecimal singleBoxSnp;
    private BigDecimal singleBoxRatedMinimumBoxCount;
    private BigDecimal singleBoxWeight;
    private BigDecimal singleBoxLayerCount;
    private BigDecimal singleBoxStorageCount;
    private BigDecimal singleBoxTheoreticalLayerCount;
    private BigDecimal singleBoxTheoreticalStorageHeight;
    private BigDecimal singleBoxThroreticalStorageCount;
    private String outerPackingPhotoIndex;
    private String outerPackingBoxType;
    private BigDecimal outerPackingLength;
    private BigDecimal outerPackingWidth;
    private BigDecimal outerPackingHeight;
    private String outerPackingSnp;
    private String outerPackingComment;
    private BigDecimal outerPackingRequiredLayers;
    private String deliveryBoxType;
    private BigDecimal deliveryBoxLength;
    private BigDecimal deliveryBoxWidth;
    private BigDecimal deliveryBoxHeight;
    private Integer isHistory;
    private Integer newestSupplyId;
    private int createPersonId;
    private Timestamp createTime;
    private Integer lastUpdatePersonId;
    private Timestamp lastUpdateTime;
    private int enabled;
    private String defaultEntryStorageLocationNo;
    private String defaultInspectionStorageLocationNo;
    private String defaultQualifiedStorageLocationNo;
    private String defaultUnqualifiedStorageLocationNo;
    private String defaultDeliveryStorageLocationNo;
    private String defaultPrepareTargetStorageLocationNo;
    private String barCodeNo;
    private BigDecimal trayCapacity;
    private String serialNo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "WarehouseID")
    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "SupplierID")
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "MaterialID")
    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    @Basic
    @Column(name = "DefaultEntryAmount")
    public BigDecimal getDefaultEntryAmount() {
        return defaultEntryAmount;
    }

    public void setDefaultEntryAmount(BigDecimal defaultEntryAmount) {
        this.defaultEntryAmount = defaultEntryAmount;
    }

    @Basic
    @Column(name = "DefaultEntryUnit")
    public String getDefaultEntryUnit() {
        return defaultEntryUnit;
    }

    public void setDefaultEntryUnit(String defaultEntryUnit) {
        this.defaultEntryUnit = defaultEntryUnit;
    }

    @Basic
    @Column(name = "DefaultEntryUnitAmount")
    public BigDecimal getDefaultEntryUnitAmount() {
        return defaultEntryUnitAmount;
    }

    public void setDefaultEntryUnitAmount(BigDecimal defaultEntryUnitAmount) {
        this.defaultEntryUnitAmount = defaultEntryUnitAmount;
    }

    @Basic
    @Column(name = "DefaultInspectionAmount")
    public BigDecimal getDefaultInspectionAmount() {
        return defaultInspectionAmount;
    }

    public void setDefaultInspectionAmount(BigDecimal defaultInspectionAmount) {
        this.defaultInspectionAmount = defaultInspectionAmount;
    }

    @Basic
    @Column(name = "DefaultInspectionUnit")
    public String getDefaultInspectionUnit() {
        return defaultInspectionUnit;
    }

    public void setDefaultInspectionUnit(String defaultInspectionUnit) {
        this.defaultInspectionUnit = defaultInspectionUnit;
    }

    @Basic
    @Column(name = "DefaultInspectionUnitAmount")
    public BigDecimal getDefaultInspectionUnitAmount() {
        return defaultInspectionUnitAmount;
    }

    public void setDefaultInspectionUnitAmount(BigDecimal defaultInspectionUnitAmount) {
        this.defaultInspectionUnitAmount = defaultInspectionUnitAmount;
    }

    @Basic
    @Column(name = "DefaultDeliveryAmount")
    public BigDecimal getDefaultDeliveryAmount() {
        return defaultDeliveryAmount;
    }

    public void setDefaultDeliveryAmount(BigDecimal defaultDeliveryAmount) {
        this.defaultDeliveryAmount = defaultDeliveryAmount;
    }

    @Basic
    @Column(name = "DefaultDeliveryUnit")
    public String getDefaultDeliveryUnit() {
        return defaultDeliveryUnit;
    }

    public void setDefaultDeliveryUnit(String defaultDeliveryUnit) {
        this.defaultDeliveryUnit = defaultDeliveryUnit;
    }

    @Basic
    @Column(name = "DefaultDeliveryUnitAmount")
    public BigDecimal getDefaultDeliveryUnitAmount() {
        return defaultDeliveryUnitAmount;
    }

    public void setDefaultDeliveryUnitAmount(BigDecimal defaultDeliveryUnitAmount) {
        this.defaultDeliveryUnitAmount = defaultDeliveryUnitAmount;
    }

    @Basic
    @Column(name = "ValidPeriod")
    public BigDecimal getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(BigDecimal validPeriod) {
        this.validPeriod = validPeriod;
    }

    @Basic
    @Column(name = "PhotoIndex")
    public String getPhotoIndex() {
        return photoIndex;
    }

    public void setPhotoIndex(String photoIndex) {
        this.photoIndex = photoIndex;
    }

    @Basic
    @Column(name = "ContainerNo")
    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    @Basic
    @Column(name = "Factory")
    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    @Basic
    @Column(name = "WorkPosition")
    public String getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
    }

    @Basic
    @Column(name = "SupplierType")
    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    @Basic
    @Column(name = "Type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "Size")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Basic
    @Column(name = "GroupPrincipal")
    public String getGroupPrincipal() {
        return groupPrincipal;
    }

    public void setGroupPrincipal(String groupPrincipal) {
        this.groupPrincipal = groupPrincipal;
    }

    @Basic
    @Column(name = "SingleBoxPhotoIndex")
    public String getSingleBoxPhotoIndex() {
        return singleBoxPhotoIndex;
    }

    public void setSingleBoxPhotoIndex(String singleBoxPhotoIndex) {
        this.singleBoxPhotoIndex = singleBoxPhotoIndex;
    }

    @Basic
    @Column(name = "SingleBoxPackagingBoxType")
    public String getSingleBoxPackagingBoxType() {
        return singleBoxPackagingBoxType;
    }

    public void setSingleBoxPackagingBoxType(String singleBoxPackagingBoxType) {
        this.singleBoxPackagingBoxType = singleBoxPackagingBoxType;
    }

    @Basic
    @Column(name = "SingleBoxLength")
    public BigDecimal getSingleBoxLength() {
        return singleBoxLength;
    }

    public void setSingleBoxLength(BigDecimal singleBoxLength) {
        this.singleBoxLength = singleBoxLength;
    }

    @Basic
    @Column(name = "SingleBoxWidth")
    public BigDecimal getSingleBoxWidth() {
        return singleBoxWidth;
    }

    public void setSingleBoxWidth(BigDecimal singleBoxWidth) {
        this.singleBoxWidth = singleBoxWidth;
    }

    @Basic
    @Column(name = "SingleBoxHeight")
    public BigDecimal getSingleBoxHeight() {
        return singleBoxHeight;
    }

    public void setSingleBoxHeight(BigDecimal singleBoxHeight) {
        this.singleBoxHeight = singleBoxHeight;
    }

    @Basic
    @Column(name = "SingleBoxSNP")
    public BigDecimal getSingleBoxSnp() {
        return singleBoxSnp;
    }

    public void setSingleBoxSnp(BigDecimal singleBoxSnp) {
        this.singleBoxSnp = singleBoxSnp;
    }

    @Basic
    @Column(name = "SingleBoxRatedMinimumBoxCount")
    public BigDecimal getSingleBoxRatedMinimumBoxCount() {
        return singleBoxRatedMinimumBoxCount;
    }

    public void setSingleBoxRatedMinimumBoxCount(BigDecimal singleBoxRatedMinimumBoxCount) {
        this.singleBoxRatedMinimumBoxCount = singleBoxRatedMinimumBoxCount;
    }

    @Basic
    @Column(name = "SingleBoxWeight")
    public BigDecimal getSingleBoxWeight() {
        return singleBoxWeight;
    }

    public void setSingleBoxWeight(BigDecimal singleBoxWeight) {
        this.singleBoxWeight = singleBoxWeight;
    }

    @Basic
    @Column(name = "SingleBoxLayerCount")
    public BigDecimal getSingleBoxLayerCount() {
        return singleBoxLayerCount;
    }

    public void setSingleBoxLayerCount(BigDecimal singleBoxLayerCount) {
        this.singleBoxLayerCount = singleBoxLayerCount;
    }

    @Basic
    @Column(name = "SingleBoxStorageCount")
    public BigDecimal getSingleBoxStorageCount() {
        return singleBoxStorageCount;
    }

    public void setSingleBoxStorageCount(BigDecimal singleBoxStorageCount) {
        this.singleBoxStorageCount = singleBoxStorageCount;
    }

    @Basic
    @Column(name = "SingleBoxTheoreticalLayerCount")
    public BigDecimal getSingleBoxTheoreticalLayerCount() {
        return singleBoxTheoreticalLayerCount;
    }

    public void setSingleBoxTheoreticalLayerCount(BigDecimal singleBoxTheoreticalLayerCount) {
        this.singleBoxTheoreticalLayerCount = singleBoxTheoreticalLayerCount;
    }

    @Basic
    @Column(name = "SingleBoxTheoreticalStorageHeight")
    public BigDecimal getSingleBoxTheoreticalStorageHeight() {
        return singleBoxTheoreticalStorageHeight;
    }

    public void setSingleBoxTheoreticalStorageHeight(BigDecimal singleBoxTheoreticalStorageHeight) {
        this.singleBoxTheoreticalStorageHeight = singleBoxTheoreticalStorageHeight;
    }

    @Basic
    @Column(name = "SingleBoxThroreticalStorageCount")
    public BigDecimal getSingleBoxThroreticalStorageCount() {
        return singleBoxThroreticalStorageCount;
    }

    public void setSingleBoxThroreticalStorageCount(BigDecimal singleBoxThroreticalStorageCount) {
        this.singleBoxThroreticalStorageCount = singleBoxThroreticalStorageCount;
    }

    @Basic
    @Column(name = "OuterPackingPhotoIndex")
    public String getOuterPackingPhotoIndex() {
        return outerPackingPhotoIndex;
    }

    public void setOuterPackingPhotoIndex(String outerPackingPhotoIndex) {
        this.outerPackingPhotoIndex = outerPackingPhotoIndex;
    }

    @Basic
    @Column(name = "OuterPackingBoxType")
    public String getOuterPackingBoxType() {
        return outerPackingBoxType;
    }

    public void setOuterPackingBoxType(String outerPackingBoxType) {
        this.outerPackingBoxType = outerPackingBoxType;
    }

    @Basic
    @Column(name = "OuterPackingLength")
    public BigDecimal getOuterPackingLength() {
        return outerPackingLength;
    }

    public void setOuterPackingLength(BigDecimal outerPackingLength) {
        this.outerPackingLength = outerPackingLength;
    }

    @Basic
    @Column(name = "OuterPackingWidth")
    public BigDecimal getOuterPackingWidth() {
        return outerPackingWidth;
    }

    public void setOuterPackingWidth(BigDecimal outerPackingWidth) {
        this.outerPackingWidth = outerPackingWidth;
    }

    @Basic
    @Column(name = "OuterPackingHeight")
    public BigDecimal getOuterPackingHeight() {
        return outerPackingHeight;
    }

    public void setOuterPackingHeight(BigDecimal outerPackingHeight) {
        this.outerPackingHeight = outerPackingHeight;
    }

    @Basic
    @Column(name = "OuterPackingSNP")
    public String getOuterPackingSnp() {
        return outerPackingSnp;
    }

    public void setOuterPackingSnp(String outerPackingSnp) {
        this.outerPackingSnp = outerPackingSnp;
    }

    @Basic
    @Column(name = "OuterPackingComment")
    public String getOuterPackingComment() {
        return outerPackingComment;
    }

    public void setOuterPackingComment(String outerPackingComment) {
        this.outerPackingComment = outerPackingComment;
    }

    @Basic
    @Column(name = "OuterPackingRequiredLayers")
    public BigDecimal getOuterPackingRequiredLayers() {
        return outerPackingRequiredLayers;
    }

    public void setOuterPackingRequiredLayers(BigDecimal outerPackingRequiredLayers) {
        this.outerPackingRequiredLayers = outerPackingRequiredLayers;
    }

    @Basic
    @Column(name = "DeliveryBoxType")
    public String getDeliveryBoxType() {
        return deliveryBoxType;
    }

    public void setDeliveryBoxType(String deliveryBoxType) {
        this.deliveryBoxType = deliveryBoxType;
    }

    @Basic
    @Column(name = "DeliveryBoxLength")
    public BigDecimal getDeliveryBoxLength() {
        return deliveryBoxLength;
    }

    public void setDeliveryBoxLength(BigDecimal deliveryBoxLength) {
        this.deliveryBoxLength = deliveryBoxLength;
    }

    @Basic
    @Column(name = "DeliveryBoxWidth")
    public BigDecimal getDeliveryBoxWidth() {
        return deliveryBoxWidth;
    }

    public void setDeliveryBoxWidth(BigDecimal deliveryBoxWidth) {
        this.deliveryBoxWidth = deliveryBoxWidth;
    }

    @Basic
    @Column(name = "DeliveryBoxHeight")
    public BigDecimal getDeliveryBoxHeight() {
        return deliveryBoxHeight;
    }

    public void setDeliveryBoxHeight(BigDecimal deliveryBoxHeight) {
        this.deliveryBoxHeight = deliveryBoxHeight;
    }

    @Basic
    @Column(name = "IsHistory")
    public Integer getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Integer isHistory) {
        this.isHistory = isHistory;
    }

    @Basic
    @Column(name = "NewestSupplyID")
    public Integer getNewestSupplyId() {
        return newestSupplyId;
    }

    public void setNewestSupplyId(Integer newestSupplyId) {
        this.newestSupplyId = newestSupplyId;
    }

    @Basic
    @Column(name = "CreatePersonID")
    public int getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(int createPersonId) {
        this.createPersonId = createPersonId;
    }

    @Basic
    @Column(name = "CreateTime")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "LastUpdatePersonID")
    public Integer getLastUpdatePersonId() {
        return lastUpdatePersonId;
    }

    public void setLastUpdatePersonId(Integer lastUpdatePersonId) {
        this.lastUpdatePersonId = lastUpdatePersonId;
    }

    @Basic
    @Column(name = "LastUpdateTime")
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Basic
    @Column(name = "Enabled")
    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name = "DefaultEntryStorageLocationNo")
    public String getDefaultEntryStorageLocationNo() {
        return defaultEntryStorageLocationNo;
    }

    public void setDefaultEntryStorageLocationNo(String defaultEntryStorageLocationNo) {
        this.defaultEntryStorageLocationNo = defaultEntryStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultInspectionStorageLocationNo")
    public String getDefaultInspectionStorageLocationNo() {
        return defaultInspectionStorageLocationNo;
    }

    public void setDefaultInspectionStorageLocationNo(String defaultInspectionStorageLocationNo) {
        this.defaultInspectionStorageLocationNo = defaultInspectionStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultQualifiedStorageLocationNo")
    public String getDefaultQualifiedStorageLocationNo() {
        return defaultQualifiedStorageLocationNo;
    }

    public void setDefaultQualifiedStorageLocationNo(String defaultQualifiedStorageLocationNo) {
        this.defaultQualifiedStorageLocationNo = defaultQualifiedStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultUnqualifiedStorageLocationNo")
    public String getDefaultUnqualifiedStorageLocationNo() {
        return defaultUnqualifiedStorageLocationNo;
    }

    public void setDefaultUnqualifiedStorageLocationNo(String defaultUnqualifiedStorageLocationNo) {
        this.defaultUnqualifiedStorageLocationNo = defaultUnqualifiedStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultDeliveryStorageLocationNo")
    public String getDefaultDeliveryStorageLocationNo() {
        return defaultDeliveryStorageLocationNo;
    }

    public void setDefaultDeliveryStorageLocationNo(String defaultDeliveryStorageLocationNo) {
        this.defaultDeliveryStorageLocationNo = defaultDeliveryStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultPrepareTargetStorageLocationNo")
    public String getDefaultPrepareTargetStorageLocationNo() {
        return defaultPrepareTargetStorageLocationNo;
    }

    public void setDefaultPrepareTargetStorageLocationNo(String defaultPrepareTargetStorageLocationNo) {
        this.defaultPrepareTargetStorageLocationNo = defaultPrepareTargetStorageLocationNo;
    }

    @Basic
    @Column(name = "BarCodeNo")
    public String getBarCodeNo() {
        return barCodeNo;
    }

    public void setBarCodeNo(String barCodeNo) {
        this.barCodeNo = barCodeNo;
    }

    @Basic
    @Column(name = "TrayCapacity")
    public BigDecimal getTrayCapacity() {
        return trayCapacity;
    }

    public void setTrayCapacity(BigDecimal trayCapacity) {
        this.trayCapacity = trayCapacity;
    }

    @Basic
    @Column(name = "SerialNo")
    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supply supply = (Supply) o;
        return id == supply.id &&
                createPersonId == supply.createPersonId &&
                enabled == supply.enabled &&
                Objects.equals(warehouseId, supply.warehouseId) &&
                Objects.equals(supplierId, supply.supplierId) &&
                Objects.equals(materialId, supply.materialId) &&
                Objects.equals(defaultEntryAmount, supply.defaultEntryAmount) &&
                Objects.equals(defaultEntryUnit, supply.defaultEntryUnit) &&
                Objects.equals(defaultEntryUnitAmount, supply.defaultEntryUnitAmount) &&
                Objects.equals(defaultInspectionAmount, supply.defaultInspectionAmount) &&
                Objects.equals(defaultInspectionUnit, supply.defaultInspectionUnit) &&
                Objects.equals(defaultInspectionUnitAmount, supply.defaultInspectionUnitAmount) &&
                Objects.equals(defaultDeliveryAmount, supply.defaultDeliveryAmount) &&
                Objects.equals(defaultDeliveryUnit, supply.defaultDeliveryUnit) &&
                Objects.equals(defaultDeliveryUnitAmount, supply.defaultDeliveryUnitAmount) &&
                Objects.equals(validPeriod, supply.validPeriod) &&
                Objects.equals(photoIndex, supply.photoIndex) &&
                Objects.equals(containerNo, supply.containerNo) &&
                Objects.equals(factory, supply.factory) &&
                Objects.equals(workPosition, supply.workPosition) &&
                Objects.equals(supplierType, supply.supplierType) &&
                Objects.equals(type, supply.type) &&
                Objects.equals(size, supply.size) &&
                Objects.equals(groupPrincipal, supply.groupPrincipal) &&
                Objects.equals(singleBoxPhotoIndex, supply.singleBoxPhotoIndex) &&
                Objects.equals(singleBoxPackagingBoxType, supply.singleBoxPackagingBoxType) &&
                Objects.equals(singleBoxLength, supply.singleBoxLength) &&
                Objects.equals(singleBoxWidth, supply.singleBoxWidth) &&
                Objects.equals(singleBoxHeight, supply.singleBoxHeight) &&
                Objects.equals(singleBoxSnp, supply.singleBoxSnp) &&
                Objects.equals(singleBoxRatedMinimumBoxCount, supply.singleBoxRatedMinimumBoxCount) &&
                Objects.equals(singleBoxWeight, supply.singleBoxWeight) &&
                Objects.equals(singleBoxLayerCount, supply.singleBoxLayerCount) &&
                Objects.equals(singleBoxStorageCount, supply.singleBoxStorageCount) &&
                Objects.equals(singleBoxTheoreticalLayerCount, supply.singleBoxTheoreticalLayerCount) &&
                Objects.equals(singleBoxTheoreticalStorageHeight, supply.singleBoxTheoreticalStorageHeight) &&
                Objects.equals(singleBoxThroreticalStorageCount, supply.singleBoxThroreticalStorageCount) &&
                Objects.equals(outerPackingPhotoIndex, supply.outerPackingPhotoIndex) &&
                Objects.equals(outerPackingBoxType, supply.outerPackingBoxType) &&
                Objects.equals(outerPackingLength, supply.outerPackingLength) &&
                Objects.equals(outerPackingWidth, supply.outerPackingWidth) &&
                Objects.equals(outerPackingHeight, supply.outerPackingHeight) &&
                Objects.equals(outerPackingSnp, supply.outerPackingSnp) &&
                Objects.equals(outerPackingComment, supply.outerPackingComment) &&
                Objects.equals(outerPackingRequiredLayers, supply.outerPackingRequiredLayers) &&
                Objects.equals(deliveryBoxType, supply.deliveryBoxType) &&
                Objects.equals(deliveryBoxLength, supply.deliveryBoxLength) &&
                Objects.equals(deliveryBoxWidth, supply.deliveryBoxWidth) &&
                Objects.equals(deliveryBoxHeight, supply.deliveryBoxHeight) &&
                Objects.equals(isHistory, supply.isHistory) &&
                Objects.equals(newestSupplyId, supply.newestSupplyId) &&
                Objects.equals(createTime, supply.createTime) &&
                Objects.equals(lastUpdatePersonId, supply.lastUpdatePersonId) &&
                Objects.equals(lastUpdateTime, supply.lastUpdateTime) &&
                Objects.equals(defaultEntryStorageLocationNo, supply.defaultEntryStorageLocationNo) &&
                Objects.equals(defaultInspectionStorageLocationNo, supply.defaultInspectionStorageLocationNo) &&
                Objects.equals(defaultQualifiedStorageLocationNo, supply.defaultQualifiedStorageLocationNo) &&
                Objects.equals(defaultUnqualifiedStorageLocationNo, supply.defaultUnqualifiedStorageLocationNo) &&
                Objects.equals(defaultDeliveryStorageLocationNo, supply.defaultDeliveryStorageLocationNo) &&
                Objects.equals(defaultPrepareTargetStorageLocationNo, supply.defaultPrepareTargetStorageLocationNo) &&
                Objects.equals(barCodeNo, supply.barCodeNo) &&
                Objects.equals(trayCapacity, supply.trayCapacity) &&
                Objects.equals(serialNo, supply.serialNo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, warehouseId, supplierId, materialId, defaultEntryAmount, defaultEntryUnit, defaultEntryUnitAmount, defaultInspectionAmount, defaultInspectionUnit, defaultInspectionUnitAmount, defaultDeliveryAmount, defaultDeliveryUnit, defaultDeliveryUnitAmount, validPeriod, photoIndex, containerNo, factory, workPosition, supplierType, type, size, groupPrincipal, singleBoxPhotoIndex, singleBoxPackagingBoxType, singleBoxLength, singleBoxWidth, singleBoxHeight, singleBoxSnp, singleBoxRatedMinimumBoxCount, singleBoxWeight, singleBoxLayerCount, singleBoxStorageCount, singleBoxTheoreticalLayerCount, singleBoxTheoreticalStorageHeight, singleBoxThroreticalStorageCount, outerPackingPhotoIndex, outerPackingBoxType, outerPackingLength, outerPackingWidth, outerPackingHeight, outerPackingSnp, outerPackingComment, outerPackingRequiredLayers, deliveryBoxType, deliveryBoxLength, deliveryBoxWidth, deliveryBoxHeight, isHistory, newestSupplyId, createPersonId, createTime, lastUpdatePersonId, lastUpdateTime, enabled, defaultEntryStorageLocationNo, defaultInspectionStorageLocationNo, defaultQualifiedStorageLocationNo, defaultUnqualifiedStorageLocationNo, defaultDeliveryStorageLocationNo, defaultPrepareTargetStorageLocationNo, barCodeNo, trayCapacity, serialNo);
    }
}
