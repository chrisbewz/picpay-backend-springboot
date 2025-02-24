package br.com.picpay.backend.data.dtos;

import org.bson.types.ObjectId;

/**
 * Record to store basic transfer information between services
 * @param payer User transfer source Id
 * @param payee User transfer destination Id
 * @param value Transfer total value
 */
public record TransferInformation(ObjectId payer, ObjectId payee, Double value) {
}
