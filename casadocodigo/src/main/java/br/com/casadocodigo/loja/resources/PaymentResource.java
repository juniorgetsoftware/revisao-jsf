package br.com.casadocodigo.loja.resources;

import java.math.BigDecimal;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import br.com.casadocodigo.loja.daos.CheckoutDAO;
import br.com.casadocodigo.loja.infra.MailSender;
import br.com.casadocodigo.loja.models.Checkout;
import br.com.casadocodigo.loja.services.InvoiceGenerator;
import br.com.casadocodigo.loja.services.PaymentGateway;

@Path("payment")
public class PaymentResource {

	private static ExecutorService executor = Executors.newFixedThreadPool(50);

	@Context
	private ServletContext ctx;

	@Inject
	private CheckoutDAO checkoutDao;

	@Inject
	private PaymentGateway paymentGateway;

	@Inject
	private MailSender mailSender;

	@Inject
	private InvoiceGenerator invoiceGenerator;

	@Inject
	private JMSContext jmsContext;

	@Resource(lookup = "java:/jms/topics/checkoutsTopic")
	private Destination checkoutsTopic;

	@Resource(name = "java:comp/DefaultManagedExecutorService")
	private ManagedExecutorService managedExecutorService;

	@POST
	public void pay(@Suspended final AsyncResponse ar, @QueryParam("uuid") String uuid) {
		String contextPath = ctx.getContextPath();
		Checkout checkout = checkoutDao.findByUuid(uuid);
		JMSProducer producer = jmsContext.createProducer();

		managedExecutorService.submit(() -> {
			BigDecimal total = checkout.getValue();
			try {
				paymentGateway.pay(total);

				producer.send(checkoutsTopic, checkout.getUuid());

				String mailBody = "Nova compra. Seu código de acompanhamento é " + checkout.getUuid();
				mailSender.send("compras@casadocodigo.com.br", checkout.getBuyer().getEmail(), "Nova compra", mailBody);
				
				invoiceGenerator.invoiceFor(checkout);

				URI redirectURI = UriBuilder.fromUri(contextPath + "/site/index.xhtml")
						.queryParam("msg", "Compra realizada com sucesso").build();
				Response response = Response.seeOther(redirectURI).build();
				ar.resume(response);
			} catch (Exception exception) {
				ar.resume(exception);
			}
		});

	}
}
