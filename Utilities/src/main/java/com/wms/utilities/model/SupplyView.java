package com.wms.utilities.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class SupplyView {
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
    private String warehouseName;
    private String materialNo;
    private String materialName;
    private String supplierNo;
    private String supplierName;
    private String createPersonName;
    private String lastUpdatePersonName;
    private Integer defaultEntryStorageLocationId;
    private Integer defaultInspectionStorageLocationId;
    private Integer defaultQualifiedStorageLocationId;
    private Integer defaultUnqualifiedStorageLocationId;
    private Integer defaultDeliveryStorageLocationId;
    private Integer defaultPrepareTargetStorageLocationId;
    private String defaultEntryStorageLocationNo;
    private String defaultEntryStorageLocationName;
    private String defaultInspectionStorageLocationNo;
    private String defaultInspectionStorageLocationName;
    private String defaultQualifiedStorageLocationNo;
    private String defaultQualifiedStorageLocationName;
    private String defaultUnqualifiedStorageLocationNo;
    private String defaultUnqualifiedStorageLocationName;
    private String defaultDeliveryStorageLocationNo;
    private String defaultDeliveryStorageLocationName;
    private String defaultPrepareTargetStorageLocationNo;
    private String defaultPrepareTargetStorageLocationName;
    private String materialProductLine;

    @Id
    @Basic
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "WarehouseID", nullable = true)
    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "SupplierID", nullable = true)
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "MaterialID", nullable = true)
    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    @Basic
    @Column(name = "DefaultEntryAmount", nullable = true, precision = 3)
    public BigDecimal getDefaultEntryAmount() {
        return defaultEntryAmount;
    }

    public void setDefaultEntryAmount(BigDecimal defaultEntryAmount) {
        this.defaultEntryAmount = defaultEntryAmount;
    }

    @Basic
    @Column(name = "DefaultEntryUnit", nullable = true, length = 64)
    public String getDefaultEntryUnit() {
        return defaultEntryUnit;
    }

    public void setDefaultEntryUnit(String defaultEntryUnit) {
        this.defaultEntryUnit = defaultEntryUnit;
    }

    @Basic
    @Column(name = "DefaultEntryUnitAmount", nullable = true, precision = 3)
    public BigDecimal getDefaultEntryUnitAmount() {
        return defaultEntryUnitAmount;
    }

    public void setDefaultEntryUnitAmount(BigDecimal defaultEntryUnitAmount) {
        this.defaultEntryUnitAmount = defaultEntryUnitAmount;
    }

    @Basic
    @Column(name = "DefaultInspectionAmount", nullable = true, precision = 3)
    public BigDecimal getDefaultInspectionAmount() {
        return defaultInspectionAmount;
    }

    public void setDefaultInspectionAmount(BigDecimal defaultInspectionAmount) {
        this.defaultInspectionAmount = defaultInspectionAmount;
    }

    @Basic
    @Column(name = "DefaultInspectionUnit", nullable = true, length = 64)
    public String getDefaultInspectionUnit() {
        return defaultInspectionUnit;
    }

    public void setDefaultInspectionUnit(String defaultInspectionUnit) {
        this.defaultInspectionUnit = defaultInspectionUnit;
    }

    @Basic
    @Column(name = "DefaultInspectionUnitAmount", nullable = true, precision = 3)
    public BigDecimal getDefaultInspectionUnitAmount() {
        return defaultInspectionUnitAmount;
    }

    public void setDefaultInspectionUnitAmount(BigDecimal defaultInspectionUnitAmount) {
        this.defaultInspectionUnitAmount = defaultInspectionUnitAmount;
    }

    @Basic
    @Column(name = "DefaultDeliveryAmount", nullable = true, precision = 3)
    public BigDecimal getDefaultDeliveryAmount() {
        return defaultDeliveryAmount;
    }

    public void setDefaultDeliveryAmount(BigDecimal defaultDeliveryAmount) {
        this.defaultDeliveryAmount = defaultDeliveryAmount;
    }

    @Basic
    @Column(name = "DefaultDeliveryUnit", nullable = true, length = 64)
    public String getDefaultDeliveryUnit() {
        return defaultDeliveryUnit;
    }

    public void setDefaultDeliveryUnit(String defaultDeliveryUnit) {
        this.defaultDeliveryUnit = defaultDeliveryUnit;
    }

    @Basic
    @Column(name = "DefaultDeliveryUnitAmount", nullable = true, precision = 3)
    public BigDecimal getDefaultDeliveryUnitAmount() {
        return defaultDeliveryUnitAmount;
    }

    public void setDefaultDeliveryUnitAmount(BigDecimal defaultDeliveryUnitAmount) {
        this.defaultDeliveryUnitAmount = defaultDeliveryUnitAmount;
    }

    @Basic
    @Column(name = "ValidPeriod", nullable = true, precision = 3)
    public BigDecimal getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(BigDecimal validPeriod) {
        this.validPeriod = validPeriod;
    }

    @Basic
    @Column(name = "PhotoIndex", nullable = true, length = 64)
    public String getPhotoIndex() {
        return photoIndex;
    }

    public void setPhotoIndex(String photoIndex) {
        this.photoIndex = photoIndex;
    }

    @Basic
    @Column(name = "ContainerNo", nullable = true, length = 64)
    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    @Basic
    @Column(name = "Factory", nullable = true, length = 64)
    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    @Basic
    @Column(name = "WorkPosition", nullable = true, length = 64)
    public String getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
    }

    @Basic
    @Column(name = "SupplierType", nullable = true, length = 64)
    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    @Basic
    @Column(name = "Type", nullable = true, length = 64)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "Size", nullable = true, length = 64)
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Basic
    @Column(name = "GroupPrincipal", nullable = true, length = 64)
    public String getGroupPrincipal() {
        return groupPrincipal;
    }

    public void setGroupPrincipal(String groupPrincipal) {
        this.groupPrincipal = groupPrincipal;
    }

    @Basic
    @Column(name = "SingleBoxPhotoIndex", nullable = true, length = 64)
    public String getSingleBoxPhotoIndex() {
        return singleBoxPhotoIndex;
    }

    public void setSingleBoxPhotoIndex(String singleBoxPhotoIndex) {
        this.singleBoxPhotoIndex = singleBoxPhotoIndex;
    }

    @Basic
    @Column(name = "SingleBoxPackagingBoxType", nullable = true, length = 64)
    public String getSingleBoxPackagingBoxType() {
        return singleBoxPackagingBoxType;
    }

    public void setSingleBoxPackagingBoxType(String singleBoxPackagingBoxType) {
        this.singleBoxPackagingBoxType = singleBoxPackagingBoxType;
    }

    @Basic
    @Column(name = "SingleBoxLength", nullable = true, precision = 3)
    public BigDecimal getSingleBoxLength() {
        return singleBoxLength;
    }

    public void setSingleBoxLength(BigDecimal singleBoxLength) {
        this.singleBoxLength = singleBoxLength;
    }

    @Basic
    @Column(name = "SingleBoxWidth", nullable = true, precision = 3)
    public BigDecimal getSingleBoxWidth() {
        return singleBoxWidth;
    }

    public void setSingleBoxWidth(BigDecimal singleBoxWidth) {
        this.singleBoxWidth = singleBoxWidth;
    }

    @Basic
    @Column(name = "SingleBoxHeight", nullable = true, precision = 3)
    public BigDecimal getSingleBoxHeight() {
        return singleBoxHeight;
    }

    public void setSingleBoxHeight(BigDecimal singleBoxHeight) {
        this.singleBoxHeight = singleBoxHeight;
    }

    @Basic
    @Column(name = "SingleBoxSNP", nullable = true, precision = 3)
    public BigDecimal getSingleBoxSnp() {
        return singleBoxSnp;
    }

    public void setSingleBoxSnp(BigDecimal singleBoxSnp) {
        this.singleBoxSnp = singleBoxSnp;
    }

    @Basic
    @Column(name = "SingleBoxRatedMinimumBoxCount", nullable = true, precision = 3)
    public BigDecimal getSingleBoxRatedMinimumBoxCount() {
        return singleBoxRatedMinimumBoxCount;
    }

    public void setSingleBoxRatedMinimumBoxCount(BigDecimal singleBoxRatedMinimumBoxCount) {
        this.singleBoxRatedMinimumBoxCount = singleBoxRatedMinimumBoxCount;
    }

    @Basic
    @Column(name = "SingleBoxWeight", nullable = true, precision = 3)
    public BigDecimal getSingleBoxWeight() {
        return singleBoxWeight;
    }

    public void setSingleBoxWeight(BigDecimal singleBoxWeight) {
        this.singleBoxWeight = singleBoxWeight;
    }

    @Basic
    @Column(name = "SingleBoxLayerCount", nullable = true, precision = 3)
    public BigDecimal getSingleBoxLayerCount() {
        return singleBoxLayerCount;
    }

    public void setSingleBoxLayerCount(BigDecimal singleBoxLayerCount) {
        this.singleBoxLayerCount = singleBoxLayerCount;
    }

    @Basic
    @Column(name = "SingleBoxStorageCount", nullable = true, precision = 3)
    public BigDecimal getSingleBoxStorageCount() {
        return singleBoxStorageCount;
    }

    public void setSingleBoxStorageCount(BigDecimal singleBoxStorageCount) {
        this.singleBoxStorageCount = singleBoxStorageCount;
    }

    @Basic
    @Column(name = "SingleBoxTheoreticalLayerCount", nullable = true, precision = 3)
    public BigDecimal getSingleBoxTheoreticalLayerCount() {
        return singleBoxTheoreticalLayerCount;
    }

    public void setSingleBoxTheoreticalLayerCount(BigDecimal singleBoxTheoreticalLayerCount) {
        this.singleBoxTheoreticalLayerCount = singleBoxTheoreticalLayerCount;
    }

    @Basic
    @Column(name = "SingleBoxTheoreticalStorageHeight", nullable = true, precision = 3)
    public BigDecimal getSingleBoxTheoreticalStorageHeight() {
        return singleBoxTheoreticalStorageHeight;
    }

    public void setSingleBoxTheoreticalStorageHeight(BigDecimal singleBoxTheoreticalStorageHeight) {
        this.singleBoxTheoreticalStorageHeight = singleBoxTheoreticalStorageHeight;
    }

    @Basic
    @Column(name = "SingleBoxThroreticalStorageCount", nullable = true, precision = 3)
    public BigDecimal getSingleBoxThroreticalStorageCount() {
        return singleBoxThroreticalStorageCount;
    }

    public void setSingleBoxThroreticalStorageCount(BigDecimal singleBoxThroreticalStorageCount) {
        this.singleBoxThroreticalStorageCount = singleBoxThroreticalStorageCount;
    }

    @Basic
    @Column(name = "OuterPackingPhotoIndex", nullable = true, length = 64)
    public String getOuterPackingPhotoIndex() {
        return outerPackingPhotoIndex;
    }

    public void setOuterPackingPhotoIndex(String outerPackingPhotoIndex) {
        this.outerPackingPhotoIndex = outerPackingPhotoIndex;
    }

    @Basic
    @Column(name = "OuterPackingBoxType", nullable = true, length = 64)
    public String getOuterPackingBoxType() {
        return outerPackingBoxType;
    }

    public void setOuterPackingBoxType(String outerPackingBoxType) {
        this.outerPackingBoxType = outerPackingBoxType;
    }

    @Basic
    @Column(name = "OuterPackingLength", nullable = true, precision = 3)
    public BigDecimal getOuterPackingLength() {
        return outerPackingLength;
    }

    public void setOuterPackingLength(BigDecimal outerPackingLength) {
        this.outerPackingLength = outerPackingLength;
    }

    @Basic
    @Column(name = "OuterPackingWidth", nullable = true, precision = 3)
    public BigDecimal getOuterPackingWidth() {
        return outerPackingWidth;
    }

    public void setOuterPackingWidth(BigDecimal outerPackingWidth) {
        this.outerPackingWidth = outerPackingWidth;
    }

    @Basic
    @Column(name = "OuterPackingHeight", nullable = true, precision = 3)
    public BigDecimal getOuterPackingHeight() {
        return outerPackingHeight;
    }

    public void setOuterPackingHeight(BigDecimal outerPackingHeight) {
        this.outerPackingHeight = outerPackingHeight;
    }

    @Basic
    @Column(name = "OuterPackingSNP", nullable = true, length = 64)
    public String getOuterPackingSnp() {
        return outerPackingSnp;
    }

    public void setOuterPackingSnp(String outerPackingSnp) {
        this.outerPackingSnp = outerPackingSnp;
    }

    @Basic
    @Column(name = "OuterPackingComment", nullable = true, length = 64)
    public String getOuterPackingComment() {
        return outerPackingComment;
    }

    public void setOuterPackingComment(String outerPackingComment) {
        this.outerPackingComment = outerPackingComment;
    }

    @Basic
    @Column(name = "OuterPackingRequiredLayers", nullable = true, precision = 3)
    public BigDecimal getOuterPackingRequiredLayers() {
        return outerPackingRequiredLayers;
    }

    public void setOuterPackingRequiredLayers(BigDecimal outerPackingRequiredLayers) {
        this.outerPackingRequiredLayers = outerPackingRequiredLayers;
    }

    @Basic
    @Column(name = "DeliveryBoxType", nullable = true, length = 64)
    public String getDeliveryBoxType() {
        return deliveryBoxType;
    }

    public void setDeliveryBoxType(String deliveryBoxType) {
        this.deliveryBoxType = deliveryBoxType;
    }

    @Basic
    @Column(name = "DeliveryBoxLength", nullable = true, precision = 3)
    public BigDecimal getDeliveryBoxLength() {
        return deliveryBoxLength;
    }

    public void setDeliveryBoxLength(BigDecimal deliveryBoxLength) {
        this.deliveryBoxLength = deliveryBoxLength;
    }

    @Basic
    @Column(name = "DeliveryBoxWidth", nullable = true, precision = 3)
    public BigDecimal getDeliveryBoxWidth() {
        return deliveryBoxWidth;
    }

    public void setDeliveryBoxWidth(BigDecimal deliveryBoxWidth) {
        this.deliveryBoxWidth = deliveryBoxWidth;
    }

    @Basic
    @Column(name = "DeliveryBoxHeight", nullable = true, precision = 3)
    public BigDecimal getDeliveryBoxHeight() {
        return deliveryBoxHeight;
    }

    public void setDeliveryBoxHeight(BigDecimal deliveryBoxHeight) {
        this.deliveryBoxHeight = deliveryBoxHeight;
    }

    @Basic
    @Column(name = "IsHistory", nullable = true)
    public Integer getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Integer isHistory) {
        this.isHistory = isHistory;
    }

    @Basic
    @Column(name = "NewestSupplyID", nullable = true)
    public Integer getNewestSupplyId() {
        return newestSupplyId;
    }

    public void setNewestSupplyId(Integer newestSupplyId) {
        this.newestSupplyId = newestSupplyId;
    }

    @Basic
    @Column(name = "CreatePersonID", nullable = false)
    public int getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(int createPersonId) {
        this.createPersonId = createPersonId;
    }

    @Basic
    @Column(name = "CreateTime", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "LastUpdatePersonID", nullable = true)
    public Integer getLastUpdatePersonId() {
        return lastUpdatePersonId;
    }

    public void setLastUpdatePersonId(Integer lastUpdatePersonId) {
        this.lastUpdatePersonId = lastUpdatePersonId;
    }

    @Basic
    @Column(name = "LastUpdateTime", nullable = true)
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Basic
    @Column(name = "Enabled", nullable = false)
    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name = "WarehouseName", nullable = true, length = 64)
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Basic
    @Column(name = "MaterialNo", nullable = true, length = 64)
    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    @Basic
    @Column(name = "MaterialName", nullable = true, length = 64)
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Basic
    @Column(name = "SupplierNo", nullable = true, length = 64)
    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    @Basic
    @Column(name = "SupplierName", nullable = true, length = 64)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Basic
    @Column(name = "CreatePersonName", nullable = true, length = 64)
    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    @Basic
    @Column(name = "LastUpdatePersonName", nullable = true, length = 64)
    public String getLastUpdatePersonName() {
        return lastUpdatePersonName;
    }

    public void setLastUpdatePersonName(String lastUpdatePersonName) {
        this.lastUpdatePersonName = lastUpdatePersonName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupplyView that = (SupplyView) o;

        if (id != that.id) return false;
        if (createPersonId != that.createPersonId) return false;
        if (enabled != that.enabled) return false;
        if (warehouseId != null ? !warehouseId.equals(that.warehouseId) : that.warehouseId != null) return false;
        if (supplierId != null ? !supplierId.equals(that.supplierId) : that.supplierId != null) return false;
        if (materialId != null ? !materialId.equals(that.materialId) : that.materialId != null) return false;
        if (defaultEntryAmount != null ? !defaultEntryAmount.equals(that.defaultEntryAmount) : that.defaultEntryAmount != null)
            return false;
        if (defaultEntryUnit != null ? !defaultEntryUnit.equals(that.defaultEntryUnit) : that.defaultEntryUnit != null)
            return false;
        if (defaultEntryUnitAmount != null ? !defaultEntryUnitAmount.equals(that.defaultEntryUnitAmount) : that.defaultEntryUnitAmount != null)
            return false;
        if (defaultInspectionAmount != null ? !defaultInspectionAmount.equals(that.defaultInspectionAmount) : that.defaultInspectionAmount != null)
            return false;
        if (defaultInspectionUnit != null ? !defaultInspectionUnit.equals(that.defaultInspectionUnit) : that.defaultInspectionUnit != null)
            return false;
        if (defaultInspectionUnitAmount != null ? !defaultInspectionUnitAmount.equals(that.defaultInspectionUnitAmount) : that.defaultInspectionUnitAmount != null)
            return false;
        if (defaultDeliveryAmount != null ? !defaultDeliveryAmount.equals(that.defaultDeliveryAmount) : that.defaultDeliveryAmount != null)
            return false;
        if (defaultDeliveryUnit != null ? !defaultDeliveryUnit.equals(that.defaultDeliveryUnit) : that.defaultDeliveryUnit != null)
            return false;
        if (defaultDeliveryUnitAmount != null ? !defaultDeliveryUnitAmount.equals(that.defaultDeliveryUnitAmount) : that.defaultDeliveryUnitAmount != null)
            return false;
        if (validPeriod != null ? !validPeriod.equals(that.validPeriod) : that.validPeriod != null) return false;
        if (photoIndex != null ? !photoIndex.equals(that.photoIndex) : that.photoIndex != null) return false;
        if (containerNo != null ? !containerNo.equals(that.containerNo) : that.containerNo != null) return false;
        if (factory != null ? !factory.equals(that.factory) : that.factory != null) return false;
        if (workPosition != null ? !workPosition.equals(that.workPosition) : that.workPosition != null) return false;
        if (supplierType != null ? !supplierType.equals(that.supplierType) : that.supplierType != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (groupPrincipal != null ? !groupPrincipal.equals(that.groupPrincipal) : that.groupPrincipal != null)
            return false;
        if (singleBoxPhotoIndex != null ? !singleBoxPhotoIndex.equals(that.singleBoxPhotoIndex) : that.singleBoxPhotoIndex != null)
            return false;
        if (singleBoxPackagingBoxType != null ? !singleBoxPackagingBoxType.equals(that.singleBoxPackagingBoxType) : that.singleBoxPackagingBoxType != null)
            return false;
        if (singleBoxLength != null ? !singleBoxLength.equals(that.singleBoxLength) : that.singleBoxLength != null)
            return false;
        if (singleBoxWidth != null ? !singleBoxWidth.equals(that.singleBoxWidth) : that.singleBoxWidth != null)
            return false;
        if (singleBoxHeight != null ? !singleBoxHeight.equals(that.singleBoxHeight) : that.singleBoxHeight != null)
            return false;
        if (singleBoxSnp != null ? !singleBoxSnp.equals(that.singleBoxSnp) : that.singleBoxSnp != null) return false;
        if (singleBoxRatedMinimumBoxCount != null ? !singleBoxRatedMinimumBoxCount.equals(that.singleBoxRatedMinimumBoxCount) : that.singleBoxRatedMinimumBoxCount != null)
            return false;
        if (singleBoxWeight != null ? !singleBoxWeight.equals(that.singleBoxWeight) : that.singleBoxWeight != null)
            return false;
        if (singleBoxLayerCount != null ? !singleBoxLayerCount.equals(that.singleBoxLayerCount) : that.singleBoxLayerCount != null)
            return false;
        if (singleBoxStorageCount != null ? !singleBoxStorageCount.equals(that.singleBoxStorageCount) : that.singleBoxStorageCount != null)
            return false;
        if (singleBoxTheoreticalLayerCount != null ? !singleBoxTheoreticalLayerCount.equals(that.singleBoxTheoreticalLayerCount) : that.singleBoxTheoreticalLayerCount != null)
            return false;
        if (singleBoxTheoreticalStorageHeight != null ? !singleBoxTheoreticalStorageHeight.equals(that.singleBoxTheoreticalStorageHeight) : that.singleBoxTheoreticalStorageHeight != null)
            return false;
        if (singleBoxThroreticalStorageCount != null ? !singleBoxThroreticalStorageCount.equals(that.singleBoxThroreticalStorageCount) : that.singleBoxThroreticalStorageCount != null)
            return false;
        if (outerPackingPhotoIndex != null ? !outerPackingPhotoIndex.equals(that.outerPackingPhotoIndex) : that.outerPackingPhotoIndex != null)
            return false;
        if (outerPackingBoxType != null ? !outerPackingBoxType.equals(that.outerPackingBoxType) : that.outerPackingBoxType != null)
            return false;
        if (outerPackingLength != null ? !outerPackingLength.equals(that.outerPackingLength) : that.outerPackingLength != null)
            return false;
        if (outerPackingWidth != null ? !outerPackingWidth.equals(that.outerPackingWidth) : that.outerPackingWidth != null)
            return false;
        if (outerPackingHeight != null ? !outerPackingHeight.equals(that.outerPackingHeight) : that.outerPackingHeight != null)
            return false;
        if (outerPackingSnp != null ? !outerPackingSnp.equals(that.outerPackingSnp) : that.outerPackingSnp != null)
            return false;
        if (outerPackingComment != null ? !outerPackingComment.equals(that.outerPackingComment) : that.outerPackingComment != null)
            return false;
        if (outerPackingRequiredLayers != null ? !outerPackingRequiredLayers.equals(that.outerPackingRequiredLayers) : that.outerPackingRequiredLayers != null)
            return false;
        if (deliveryBoxType != null ? !deliveryBoxType.equals(that.deliveryBoxType) : that.deliveryBoxType != null)
            return false;
        if (deliveryBoxLength != null ? !deliveryBoxLength.equals(that.deliveryBoxLength) : that.deliveryBoxLength != null)
            return false;
        if (deliveryBoxWidth != null ? !deliveryBoxWidth.equals(that.deliveryBoxWidth) : that.deliveryBoxWidth != null)
            return false;
        if (deliveryBoxHeight != null ? !deliveryBoxHeight.equals(that.deliveryBoxHeight) : that.deliveryBoxHeight != null)
            return false;
        if (isHistory != null ? !isHistory.equals(that.isHistory) : that.isHistory != null) return false;
        if (newestSupplyId != null ? !newestSupplyId.equals(that.newestSupplyId) : that.newestSupplyId != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (lastUpdatePersonId != null ? !lastUpdatePersonId.equals(that.lastUpdatePersonId) : that.lastUpdatePersonId != null)
            return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(that.lastUpdateTime) : that.lastUpdateTime != null)
            return false;
        if (warehouseName != null ? !warehouseName.equals(that.warehouseName) : that.warehouseName != null)
            return false;
        if (materialNo != null ? !materialNo.equals(that.materialNo) : that.materialNo != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (supplierNo != null ? !supplierNo.equals(that.supplierNo) : that.supplierNo != null) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;
        if (createPersonName != null ? !createPersonName.equals(that.createPersonName) : that.createPersonName != null)
            return false;
        if (lastUpdatePersonName != null ? !lastUpdatePersonName.equals(that.lastUpdatePersonName) : that.lastUpdatePersonName != null)
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
        result = 31 * result + (warehouseName != null ? warehouseName.hashCode() : 0);
        result = 31 * result + (materialNo != null ? materialNo.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (supplierNo != null ? supplierNo.hashCode() : 0);
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (createPersonName != null ? createPersonName.hashCode() : 0);
        result = 31 * result + (lastUpdatePersonName != null ? lastUpdatePersonName.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "DefaultEntryStorageLocationID", nullable = true)
    public Integer getDefaultEntryStorageLocationId() {
        return defaultEntryStorageLocationId;
    }

    public void setDefaultEntryStorageLocationId(Integer defaultEntryStorageLocationId) {
        this.defaultEntryStorageLocationId = defaultEntryStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultInspectionStorageLocationID", nullable = true)
    public Integer getDefaultInspectionStorageLocationId() {
        return defaultInspectionStorageLocationId;
    }

    public void setDefaultInspectionStorageLocationId(Integer defaultInspectionStorageLocationId) {
        this.defaultInspectionStorageLocationId = defaultInspectionStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultQualifiedStorageLocationID", nullable = true)
    public Integer getDefaultQualifiedStorageLocationId() {
        return defaultQualifiedStorageLocationId;
    }

    public void setDefaultQualifiedStorageLocationId(Integer defaultQualifiedStorageLocationId) {
        this.defaultQualifiedStorageLocationId = defaultQualifiedStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultUnqualifiedStorageLocationID", nullable = true)
    public Integer getDefaultUnqualifiedStorageLocationId() {
        return defaultUnqualifiedStorageLocationId;
    }

    public void setDefaultUnqualifiedStorageLocationId(Integer defaultUnqualifiedStorageLocationId) {
        this.defaultUnqualifiedStorageLocationId = defaultUnqualifiedStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultDeliveryStorageLocationID", nullable = true)
    public Integer getDefaultDeliveryStorageLocationId() {
        return defaultDeliveryStorageLocationId;
    }

    public void setDefaultDeliveryStorageLocationId(Integer defaultDeliveryStorageLocationId) {
        this.defaultDeliveryStorageLocationId = defaultDeliveryStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultPrepareTargetStorageLocationID", nullable = true)
    public Integer getDefaultPrepareTargetStorageLocationId() {
        return defaultPrepareTargetStorageLocationId;
    }

    public void setDefaultPrepareTargetStorageLocationId(Integer defaultPrepareTargetStorageLocationId) {
        this.defaultPrepareTargetStorageLocationId = defaultPrepareTargetStorageLocationId;
    }

    @Basic
    @Column(name = "DefaultEntryStorageLocationNo", nullable = true, length = 64)
    public String getDefaultEntryStorageLocationNo() {
        return defaultEntryStorageLocationNo;
    }

    public void setDefaultEntryStorageLocationNo(String defaultEntryStorageLocationNo) {
        this.defaultEntryStorageLocationNo = defaultEntryStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultEntryStorageLocationName", nullable = true, length = 64)
    public String getDefaultEntryStorageLocationName() {
        return defaultEntryStorageLocationName;
    }

    public void setDefaultEntryStorageLocationName(String defaultEntryStorageLocationName) {
        this.defaultEntryStorageLocationName = defaultEntryStorageLocationName;
    }

    @Basic
    @Column(name = "DefaultInspectionStorageLocationNo", nullable = true, length = 64)
    public String getDefaultInspectionStorageLocationNo() {
        return defaultInspectionStorageLocationNo;
    }

    public void setDefaultInspectionStorageLocationNo(String defaultInspectionStorageLocationNo) {
        this.defaultInspectionStorageLocationNo = defaultInspectionStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultInspectionStorageLocationName", nullable = true, length = 64)
    public String getDefaultInspectionStorageLocationName() {
        return defaultInspectionStorageLocationName;
    }

    public void setDefaultInspectionStorageLocationName(String defaultInspectionStorageLocationName) {
        this.defaultInspectionStorageLocationName = defaultInspectionStorageLocationName;
    }

    @Basic
    @Column(name = "DefaultQualifiedStorageLocationNo", nullable = true, length = 64)
    public String getDefaultQualifiedStorageLocationNo() {
        return defaultQualifiedStorageLocationNo;
    }

    public void setDefaultQualifiedStorageLocationNo(String defaultQualifiedStorageLocationNo) {
        this.defaultQualifiedStorageLocationNo = defaultQualifiedStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultQualifiedStorageLocationName", nullable = true, length = 64)
    public String getDefaultQualifiedStorageLocationName() {
        return defaultQualifiedStorageLocationName;
    }

    public void setDefaultQualifiedStorageLocationName(String defaultQualifiedStorageLocationName) {
        this.defaultQualifiedStorageLocationName = defaultQualifiedStorageLocationName;
    }

    @Basic
    @Column(name = "DefaultUnqualifiedStorageLocationNo", nullable = true, length = 64)
    public String getDefaultUnqualifiedStorageLocationNo() {
        return defaultUnqualifiedStorageLocationNo;
    }

    public void setDefaultUnqualifiedStorageLocationNo(String defaultUnqualifiedStorageLocationNo) {
        this.defaultUnqualifiedStorageLocationNo = defaultUnqualifiedStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultUnqualifiedStorageLocationName", nullable = true, length = 64)
    public String getDefaultUnqualifiedStorageLocationName() {
        return defaultUnqualifiedStorageLocationName;
    }

    public void setDefaultUnqualifiedStorageLocationName(String defaultUnqualifiedStorageLocationName) {
        this.defaultUnqualifiedStorageLocationName = defaultUnqualifiedStorageLocationName;
    }

    @Basic
    @Column(name = "DefaultDeliveryStorageLocationNo", nullable = true, length = 64)
    public String getDefaultDeliveryStorageLocationNo() {
        return defaultDeliveryStorageLocationNo;
    }

    public void setDefaultDeliveryStorageLocationNo(String defaultDeliveryStorageLocationNo) {
        this.defaultDeliveryStorageLocationNo = defaultDeliveryStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultDeliveryStorageLocationName", nullable = true, length = 64)
    public String getDefaultDeliveryStorageLocationName() {
        return defaultDeliveryStorageLocationName;
    }

    public void setDefaultDeliveryStorageLocationName(String defaultDeliveryStorageLocationName) {
        this.defaultDeliveryStorageLocationName = defaultDeliveryStorageLocationName;
    }

    @Basic
    @Column(name = "DefaultPrepareTargetStorageLocationNo", nullable = true, length = 64)
    public String getDefaultPrepareTargetStorageLocationNo() {
        return defaultPrepareTargetStorageLocationNo;
    }

    public void setDefaultPrepareTargetStorageLocationNo(String defaultPrepareTargetStorageLocationNo) {
        this.defaultPrepareTargetStorageLocationNo = defaultPrepareTargetStorageLocationNo;
    }

    @Basic
    @Column(name = "DefaultPrepareTargetStorageLocationName", nullable = true, length = 64)
    public String getDefaultPrepareTargetStorageLocationName() {
        return defaultPrepareTargetStorageLocationName;
    }

    public void setDefaultPrepareTargetStorageLocationName(String defaultPrepareTargetStorageLocationName) {
        this.defaultPrepareTargetStorageLocationName = defaultPrepareTargetStorageLocationName;
    }

    @Basic
    @Column(name = "MaterialProductLine", nullable = true, length = 64)
    public String getMaterialProductLine() {
        return materialProductLine;
    }

    public void setMaterialProductLine(String materialProductLine) {
        this.materialProductLine = materialProductLine;
    }
}
