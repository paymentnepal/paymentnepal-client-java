Library for working with https://paymentnepal.com/
=============

The library contains the following main classes:

PaymentNepalService - service for getting available payment methods, initiating a transaction, checking the status, etc.

Init Payment Request - request parameters for initiating a transaction.

InitPaymentAnswer-the result of the transaction initiation, contains such fields as the transaction ID and the session key to get the transaction status.

TransactionDetails-result of checking the transaction status.

RefundRequest - request parameters for making a refund.

RefundResponse - the result of the refund.

CardTokenRequest-parameters of the request to receive the card data token.

CardTokenResponse - the result of creating the token.

During operation, two exceptions can be triggered: PaymentNepalTemporaryError and PaymentNepalFatalError.

PaymentNepalTemporaryError is triggered if a temporary error occurs.

PaymentNepalFatalError is triggered if the error is fatal and it makes no sense to try to repeat the operation.

Example of the transaction initiation:

    PaymentNepalService service = new PaymentNepalService("<KEY>");
    InitPaymentRequest request = new InitPaymentRequest()
            .builder()
            .currency("NPR")
            .paymentType("spg")
            .cost(new BigDecimal(10.5))
            .name("Test")
            .email("main@example.com")
            .phone("71111111111")
            .build();
    InitPaymentResponse response = service.initPayment(request);

Getting the transaction status:

    TransactionDetails details = service.transactionDetails(response.getSessionKey());
    if (details.getStatus() == TransactionStatus.PAYED || details.getStatus() == TransactionStatus.SUCCESS) {
      // transaction is paid
    } else {
      // transaction is not paid
    }

To make a payment by bank card with the input of card data, you must first create a token containing the data, and then use it to initiate the payment.
The data required to create the token is contained in the CardTokenRequest.
If the card requires 3-D Secure verification, the InitPaymentResponse will contain the card3ds field with data for a POST request to the issuing bank's website.

Example of creating a token:

       PaymentNepalService service = new PaymentNepalService("<KEY>");
       CardTokenRequest request = new CardTokenRequest(<Service ID>, "<Card number>", <Month>, "<Year>", "<CVC>", "<Cardholder>");
       CardTokenResponse response = service.createCardToken(request, true); // true - token for test payment

If the token could not be created, an exception will either be generated - in case of authorization problems, network problems, etc.
Or the response.hasErrors() method will return true, in this case the data did not pass validation, the error details
can be found in response.errors or by calling the getCardErrors, getCardHolderErrors, getExMonthErrors, getExpYearErrors and getCvcErrors methods.

Initiating a transaction using a token:

       InitPaymentRequest request = InitPaymentRequest.builder()
                    .paymentType("spg_test")
                    .cost(new BigDecimal(10.5))
                    .name("Test")
                    .cardToken(response.getToken())
                    .build();
       InitPaymentResponse response = alba.initPayment(request);

Checking whether 3-D Secure is required:

       Card3ds card3ds = response.getCard3ds();
       if (card3ds != null) {
           // 3-D secure required
       }

If 3-D secure is required, then you need to make a POST request to the cad 3ds.getAcsUrl() address with the parameters:

       PAReq-with the value card3ds. getPaReq ()
       MD - with the value card3ds.getMd()
       TermUrl - The URL of the handler on your site. The user will be returned to it after passing 3DS authorization on the website of the card issuing bank. This URL should be formed so that it transmits information about the transaction: it is recommended to pass service_id, tid and order_id (if the transaction was created with it).

3DS authorization result handler (Term Url) in GET parameters gets previously generated transaction information (service_id, tid, order_id), in POST parameters gets information from the issuing bank - fields PaRes and MD;

To confirm the passage of 3DS authorization, you call the POST API request https://partner.paymentnepal.com/alba/ack3ds/, passing there the following:

        service_id;
        tid или order_id;
        emitent_response - data received from the issuing bank in the form of a JSON-encoded dictionary (Contained in card3ds)

Request authorization: signature version 2.0+ or via API_key Result of the API /alba/ack3ds/ method:

1. If it fails, the response will be returned in the form of JSON:

        {"status": "error", "message": "ERROR DESCRIPTION"}

2. Upon successful verification, the response will be returned in the form of JSON:

        {"status": "success"}


It is also possible to use a ready-made termurl, just specify:

         https://secure.paymentnepal.com/acquire?sid=<ID-Сервиса>&oid=<ID-Транзакции>&op=pay

Or in the case of a test payment:

         https://test.paymentnepal.com/acquire?sid=<ID-Сервиса>&oid=<ID-Транзакции>&op=pay

Then the user will be directed through the bank's form to the URL of the successful purchase / error page of the service.


Recurring Payments
-------------

Recurring payments allow you to make periodic debits without entering additional card data.

The first call must contain a token, a link to a detailed description of the rules for providing a recurring payment,
and a text description of what the RP is registered for.

Example:

         InitPaymentRequest request = InitPaymentRequest
                .builder()
                ...
                .recurrentParams(RecurrentParams.first("<URL>", "<COMMENT>"))
                .cardToken(response.getToken())
                .orderId("1000")
                ...

Subsequent calls must contain the orderId that was used in the first call.

Example:

         InitPaymentRequest request = InitPaymentRequest
                .builder()
                ...
                .recurrentParams(RecurrentParams.next("1000"))
                ...

A complete demonstration of the library: https://github.com/paymentnepal/paymentnepal-android-example

