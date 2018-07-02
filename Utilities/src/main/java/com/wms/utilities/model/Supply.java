package com.wms.utilities.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
    private Integer defaultEntryStorageLocationId;
    private Integer defaultInspectionStorageLocationId;
    private Integer defaultQualifiedStorageLocationId;
    private Integer defaultUnqualifiedStorageLocationId;
    private Integer defaultDeliveryStorageLocationId;
    private Integer defaultPrepareTargetStorageLocationId;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "DefaultEntryStorageLocationID")
    public Integer getDefaultEntryStorageLocationId() {
        return defaultEntryStorageLocationId;
    }

    public void setDefaultEntryStorageLocationId(Integer defaultEntryStorageLocationId) {
        this.defaultEntryStorageLocationId = defaultEntryStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultInspectionStorageLocationID")
    public Integer getDefaultInspectionStorageLocationId() {
        return defaultInspectionStorageLocationId;
    }

    public void setDefaultInspectionStorageLocationId(Integer defaultInspectionStorageLocationId) {
        this.defaultInspectionStorageLocationId = defaultInspectionStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultQualifiedStorageLocationID")
    public Integer getDefaultQualifiedStorageLocationId() {
        return defaultQualifiedStorageLocationId;
    }

    public void setDefaultQualifiedStorageLocationId(Integer defaultQualifiedStorageLocationId) {
        this.defaultQualifiedStorageLocationId = defaultQualifiedStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultUnqualifiedStorageLocationID")
    public Integer getDefaultUnqualifiedStorageLocationId() {
        return defaultUnqualifiedStorageLocationId;
    }

    public void setDefaultUnqualifiedStorageLocationId(Integer defaultUnqualifiedStorageLocationId) {
        this.defaultUnqualifiedStorageLocationId = defaultUnqualifiedStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultDeliveryStorageLocationID")
    public Integer getDefaultDeliveryStorageLocationId() {
        return defaultDeliveryStorageLocationId;
    }

    public void setDefaultDeliveryStorageLocationId(Integer defaultDeliveryStorageLocationId) {
        this.defaultDeliveryStorageLocationId = defaultDeliveryStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultPrepareTargetStorageLocationID")
    public Integer getDefaultPrepareTargetStorageLocationId() {
        return defaultPrepareTargetStorageLocationId;
    }

    public void setDefaultPrepareTargetStorageLocationId(Integer defaultPrepareTargetStorageLocationId) {
        this.defaultPrepareTargetStorageLocationId = defaultPrepareTargetStorageLocationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supply supply = (Supply) o;

        if (id != supply.id) return false;
        if (createPersonId != supply.createPersonId) return false;
        if (enabled != supply.enabled) return false;
        if (warehouseId != null ? !warehouseId.equals(supply.warehouseId) : supply.warehouseId != null) return false;
        if (supplierId != null ? !supplierId.equals(supply.supplierId) : supply.supplierId != null) return false;
        if (materialId != null ? !materialId.equals(supply.materialId) : supply.materialId != null) return false;
        if (defaultEntryAmount != null ? !defaultEntryAmount.equals(supply.defaultEntryAmount) : supply.defaultEntryAmount != null)
            return false;
        if (defaultEntryUnit != null ? !defaultEntryUnit.equals(supply.defaultEntryUnit) : supply.defaultEntryUnit != null)
            return false;
        if (defaultEntryUnitAmount != null ? !defaultEntryUnitAmount.equals(supply.defaultEntryUnitAmount) : supply.defaultEntryUnitAmount != null)
            return false;
        if (defaultInspectionAmount != null ? !defaultInspectionAmount.equals(supply.defaultInspectionAmount) : supply.defaultInspectionAmount != null)
            return false;
        if (defaultInspectionUnit != null ? !defaultInspectionUnit.equals(supply.defaultInspectionUnit) : supply.defaultInspectionUnit != null)
            return false;
        if (defaultInspectionUnitAmount != null ? !defaultInspectionUnitAmount.equals(supply.defaultInspectionUnitAmount) : supply.defaultInspectionUnitAmount != null)
            return false;
        if (defaultDeliveryAmount != null ? !defaultDeliveryAmount.equals(supply.defaultDeliveryAmount) : supply.defaultDeliveryAmount != null)
            return false;
        if (defaultDeliveryUnit != null ? !defaultDeliveryUnit.equals(supply.defaultDeliveryUnit) : supply.defaultDeliveryUnit != null)
            return false;
        if (defaultDeliveryUnitAmount != null ? !defaultDeliveryUnitAmount.equals(supply.defaultDeliveryUnitAmount) : supply.defaultDeliveryUnitAmount != null)
            return false;
        if (validPeriod != null ? !validPeriod.equals(supply.validPeriod) : supply.validPeriod != null) return false;
        if (photoIndex != null ? !photoIndex.equals(supply.photoIndex) : supply.photoIndex != null) return false;
        if (containerNo != null ? !containerNo.equals(supply.containerNo) : supply.containerNo != null) return false;
        if (factory != null ? !factory.equals(supply.factory) : supply.factory != null) return false;
        if (workPosition != null ? !workPosition.equals(supply.workPosition) : supply.workPosition != null)
            return false;
        if (supplierType != null ? !supplierType.equals(supply.supplierType) : supply.supplierType != null)
            return false;
        if (type != null ? !type.equals(supply.type) : supply.type != null) return false;
        if (size != null ? !size.equals(supply.size) : supply.size != null) return false;
        if (groupPrincipal != null ? !groupPrincipal.equals(supply.groupPrincipal) : supply.groupPrincipal != null)
            return false;
        if (singleBoxPhotoIndex != null ? !singleBoxPhotoIndex.equals(supply.singleBoxPhotoIndex) : supply.singleBoxPhotoIndex != null)
            return false;
        if (singleBoxPackagingBoxType != null ? !singleBoxPackagingBoxType.equals(supply.singleBoxPackagingBoxType) : supply.singleBoxPackagingBoxType != null)
            return false;
        if (singleBoxLength != null ? !singleBoxLength.equals(supply.singleBoxLength) : supply.singleBoxLength != null)
            return false;
        if (singleBoxWidth != null ? !singleBoxWidth.equals(supply.singleBoxWidth) : supply.singleBoxWidth != null)
            return false;
        if (singleBoxHeight != null ? !singleBoxHeight.equals(supply.singleBoxHeight) : supply.singleBoxHeight != null)
            return false;
        if (singleBoxSnp != null ? !singleBoxSnp.equals(supply.singleBoxSnp) : supply.singleBoxSnp != null)
            return false;
        if (singleBoxRatedMinimumBoxCount != null ? !singleBoxRatedMinimumBoxCount.equals(supply.singleBoxRatedMinimumBoxCount) : supply.singleBoxRatedMinimumBoxCount != null)
            return false;
        if (singleBoxWeight != null ? !singleBoxWeight.equals(supply.singleBoxWeight) : supply.singleBoxWeight != null)
            return false;
        if (singleBoxLayerCount != null ? !singleBoxLayerCount.equals(supply.singleBoxLayerCount) : supply.singleBoxLayerCount != null)
            return false;
        if (singleBoxStorageCount != null ? !singleBoxStorageCount.equals(supply.singleBoxStorageCount) : supply.singleBoxStorageCount != null)
            return false;
        if (singleBoxTheoreticalLayerCount != null ? !singleBoxTheoreticalLayerCount.equals(supply.singleBoxTheoreticalLayerCount) : supply.singleBoxTheoreticalLayerCount != null)
            return false;
        if (singleBoxTheoreticalStorageHeight != null ? !singleBoxTheoreticalStorageHeight.equals(supply.singleBoxTheoreticalStorageHeight) : supply.singleBoxTheoreticalStorageHeight != null)
            return false;
        if (singleBoxThroreticalStorageCount != null ? !singleBoxThroreticalStorageCount.equals(supply.singleBoxThroreticalStorageCount) : supply.singleBoxThroreticalStorageCount != null)
            return false;
        if (outerPackingPhotoIndex != null ? !outerPackingPhotoIndex.equals(supply.outerPackingPhotoIndex) : supply.outerPackingPhotoIndex != null)
            return false;
        if (outerPackingBoxType != null ? !outerPackingBoxType.equals(supply.outerPackingBoxType) : supply.outerPackingBoxType != null)
            return false;
        if (outerPackingLength != null ? !outerPackingLength.equals(supply.outerPackingLength) : supply.outerPackingLength != null)
            return false;
        if (outerPackingWidth != null ? !outerPackingWidth.equals(supply.outerPackingWidth) : supply.outerPackingWidth != null)
            return false;
        if (outerPackingHeight != null ? !outerPackingHeight.equals(supply.outerPackingHeight) : supply.outerPackingHeight != null)
            return false;
        if (outerPackingSnp != null ? !outerPackingSnp.equals(supply.outerPackingSnp) : supply.outerPackingSnp != null)
            return false;
        if (outerPackingComment != null ? !outerPackingComment.equals(supply.outerPackingComment) : supply.outerPackingComment != null)
            return false;
        if (outerPackingRequiredLayers != null ? !outerPackingRequiredLayers.equals(supply.outerPackingRequiredLayers) : supply.outerPackingRequiredLayers != null)
            return false;
        if (deliveryBoxType != null ? !deliveryBoxType.equals(supply.deliveryBoxType) : supply.deliveryBoxType != null)
            return false;
        if (deliveryBoxLength != null ? !deliveryBoxLength.equals(supply.deliveryBoxLength) : supply.deliveryBoxLength != null)
            return false;
        if (deliveryBoxWidth != null ? !deliveryBoxWidth.equals(supply.deliveryBoxWidth) : supply.deliveryBoxWidth != null)
            return false;
        if (deliveryBoxHeight != null ? !deliveryBoxHeight.equals(supply.deliveryBoxHeight) : supply.deliveryBoxHeight != null)
            return false;
        if (isHistory != null ? !isHistory.equals(supply.isHistory) : supply.isHistory != null) return false;
        if (newestSupplyId != null ? !newestSupplyId.equals(supply.newestSupplyId) : supply.newestSupplyId != null)
            return false;
        if (createTime != null ? !createTime.equals(supply.createTime) : supply.createTime != null) return false;
        if (lastUpdatePersonId != null ? !lastUpdatePersonId.equals(supply.lastUpdatePersonId) : supply.lastUpdatePersonId != null)
            return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(supply.lastUpdateTime) : supply.lastUpdateTime != null)
            return false;
        if (defaultEntryStorageLocationId != null ? !defaultEntryStorageLocationId.equals(supply.defaultEntryStorageLocationId) : supply.defaultEntryStorageLocationId != null)
            return false;
        if (defaultInspectionStorageLocationId != null ? !defaultInspectionStorageLocationId.equals(supply.defaultInspectionStorageLocationId) : supply.defaultInspectionStorageLocationId != null)
            return false;
        if (defaultQualifiedStorageLocationId != null ? !defaultQualifiedStorageLocationId.equals(supply.defaultQualifiedStorageLocationId) : supply.defaultQualifiedStorageLocationId != null)
            return false;
        if (defaultUnqualifiedStorageLocationId != null ? !defaultUnqualifiedStorageLocationId.equals(supply.defaultUnqualifiedStorageLocationId) : supply.defaultUnqualifiedStorageLocationId != null)
            return false;
        if (defaultDeliveryStorageLocationId != null ? !defaultDeliveryStorageLocationId.equals(supply.defaultDeliveryStorageLocationId) : supply.defaultDeliveryStorageLocationId != null)
            return false;
        if (defaultPrepareTargetStorageLocationId != null ? !defaultPrepareTargetStorageLocationId.equals(supply.defaultPrepareTargetStorageLocationId) : supply.defaultPrepareTargetStorageLocationId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (warehouseId != null ? warehouseId.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        result = 31 * result + (defaultEntryAmount != null ? defaultEntryAmount.hashCode() : 0);
        result = 31 * result + (defaultEntryUnit != null ? defaultEntryUnit.hashCode() : 0);
        result = 31 * result + (defaultEntryUnitAmount != null ? defaultEntryUnitAmount.hashCode() : 0);
        result = 31 * result + (defaultInspectionAmount != null ? defaultInspectionAmount.hashCode() : 0);
        result = 31 * result + (defaultInspectionUnit != null ? defaultInspectionUnit.hashCode() : 0);
        result = 31 * result + (defaultInspectionUnitAmount != null ? defaultInspectionUnitAmount.hashCode() : 0);
        result = 31 * result + (defaultDeliveryAmount != null ? defaultDeliveryAmount.hashCode() : 0);
        result = 31 * result + (defaultDeliveryUnit != null ? defaultDeliveryUnit.hashCode() : 0);
        result = 31 * result + (defaultDeliveryUnitAmount != null ? defaultDeliveryUnitAmount.hashCode() : 0);
        result = 31 * result + (validPeriod != null ? validPeriod.hashCode() : 0);
        result = 31 * result + (photoIndex != null ? photoIndex.hashCode() : 0);
        result = 31 * result + (containerNo != null ? containerNo.hashCode() : 0);
        result = 31 * result + (factory != null ? factory.hashCode() : 0);
        result = 31 * result + (workPosition != null ? workPosition.hashCode() : 0);
        result = 31 * result + (supplierType != null ? supplierType.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (groupPrincipal != null ? groupPrincipal.hashCode() : 0);
        result = 31 * result + (singleBoxPhotoIndex != null ? singleBoxPhotoIndex.hashCode() : 0);
        result = 31 * result + (singleBoxPackagingBoxType != null ? singleBoxPackagingBoxType.hashCode() : 0);
        result = 31 * result + (singleBoxLength != null ? singleBoxLength.hashCode() : 0);
        result = 31 * result + (singleBoxWidth != null ? singleBoxWidth.hashCode() : 0);
        result = 31 * result + (singleBoxHeight != null ? singleBoxHeight.hashCode() : 0);
        result = 31 * result + (singleBoxSnp != null ? singleBoxSnp.hashCode() : 0);
        result = 31 * result + (singleBoxRatedMinimumBoxCount != null ? singleBoxRatedMinimumBoxCount.hashCode() : 0);
        result = 31 * result + (singleBoxWeight != null ? singleBoxWeight.hashCode() : 0);
        result = 31 * result + (singleBoxLayerCount != null ? singleBoxLayerCount.hashCode() : 0);
        result = 31 * result + (singleBoxStorageCount != null ? singleBoxStorageCount.hashCode() : 0);
        result = 31 * result + (singleBoxTheoreticalLayerCount != null ? singleBoxTheoreticalLayerCount.hashCode() : 0);
        result = 31 * result + (singleBoxTheoreticalStorageHeight != null ? singleBoxTheoreticalStorageHeight.hashCode() : 0);
        result = 31 * result + (singleBoxThroreticalStorageCount != null ? singleBoxThroreticalStorageCount.hashCode() : 0);
        result = 31 * result + (outerPackingPhotoIndex != null ? outerPackingPhotoIndex.hashCode() : 0);
        result = 31 * result + (outerPackingBoxType != null ? outerPackingBoxType.hashCode() : 0);
        result = 31 * result + (outerPackingLength != null ? outerPackingLength.hashCode() : 0);
        result = 31 * result + (outerPackingWidth != null ? outerPackingWidth.hashCode() : 0);
        result = 31 * result + (outerPackingHeight != null ? outerPackingHeight.hashCode() : 0);
        result = 31 * result + (outerPackingSnp != null ? outerPackingSnp.hashCode() : 0);
        result = 31 * result + (outerPackingComment != null ? outerPackingComment.hashCode() : 0);
        result = 31 * result + (outerPackingRequiredLayers != null ? outerPackingRequiredLayers.hashCode() : 0);
        result = 31 * result + (deliveryBoxType != null ? deliveryBoxType.hashCode() : 0);
        result = 31 * result + (deliveryBoxLength != null ? deliveryBoxLength.hashCode() : 0);
        result = 31 * result + (deliveryBoxWidth != null ? deliveryBoxWidth.hashCode() : 0);
        result = 31 * result + (deliveryBoxHeight != null ? deliveryBoxHeight.hashCode() : 0);
        result = 31 * result + (isHistory != null ? isHistory.hashCode() : 0);
        result = 31 * result + (newestSupplyId != null ? newestSupplyId.hashCode() : 0);
        result = 31 * result + createPersonId;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastUpdatePersonId != null ? lastUpdatePersonId.hashCode() : 0);
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        result = 31 * result + enabled;
        result = 31 * result + (defaultEntryStorageLocationId != null ? defaultEntryStorageLocationId.hashCode() : 0);
        result = 31 * result + (defaultInspectionStorageLocationId != null ? defaultInspectionStorageLocationId.hashCode() : 0);
        result = 31 * result + (defaultQualifiedStorageLocationId != null ? defaultQualifiedStorageLocationId.hashCode() : 0);
        result = 31 * result + (defaultUnqualifiedStorageLocationId != null ? defaultUnqualifiedStorageLocationId.hashCode() : 0);
        result = 31 * result + (defaultDeliveryStorageLocationId != null ? defaultDeliveryStorageLocationId.hashCode() : 0);
        result = 31 * result + (defaultPrepareTargetStorageLocationId != null ? defaultPrepareTargetStorageLocationId.hashCode() : 0);
        return result;
    }
}
