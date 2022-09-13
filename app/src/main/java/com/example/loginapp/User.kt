package com.example.loginapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "User"
)
class User {

    @PrimaryKey
    var surveyId: Int = 0

    @ColumnInfo(name = "Latitude/Longitude")
    var latLong: String? = null

    @ColumnInfo(name = "Surveyor Name")
    var surveyorName: String? = null

    @ColumnInfo(name = "Zone")
    var zone: String? = null

    @ColumnInfo(name = "District")
    var district: String? = null

    @ColumnInfo(name = "Structure Type")
    var structureType: String? = null

    @ColumnInfo(name = "Construction Type")
    var constructionType: String? = null

    @ColumnInfo(name = "Road Classification")
    var roadClassification: String? = null

    @ColumnInfo(name = "Road Number")
    var roadNumber: String? = null

    @ColumnInfo(name = "Start Easting")
    var startEasting: String? = null

    @ColumnInfo(name = "Start Northing")
    var startNorthing: String? = null

    @ColumnInfo(name = "End Northing")
    var endNorthing: String? = null

    @ColumnInfo(name = "End Easting")
    var endEasting: String? = null

    @ColumnInfo(name = "No Of Span")
    var noOfSpan: String? = null

    @ColumnInfo(name = "Width Of Structure")
    var widthOfStructure: String? = null

    @ColumnInfo(name = "Span Length")
    var spanLength: String? = null

    @ColumnInfo(name = "Over All Collection")
    var overAllCollection: String? = null

    @ColumnInfo(name = "Date")
    var date: String? = null


}