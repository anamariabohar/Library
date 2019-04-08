package org.sda.librarymanagement.service;

import java.util.List;

import org.sda.librarymanagement.entity.Client;
import org.sda.librarymanagement.repository.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

//	public Client getLoginInfo(Client client) {
//		return clientRepository.getLoginInfo(client);
//	}

	public List<Client> getAllClients() {
		return clientRepository.findAll();
	}

	public Client getOneClientById(Long id) {
		return clientRepository.getOne(id);
	}

	public void saveClient(Client client) {
		clientRepository.saveAndFlush(client);
	}

	public Client updateBook(@PathVariable Long id, @RequestBody Client client) {
		Client existingClient = clientRepository.getOne(id);
		BeanUtils.copyProperties(client, existingClient);
		return clientRepository.saveAndFlush(existingClient);
	}

	public Client deleteBook(@PathVariable Long id) {
		Client existingClient = clientRepository.getOne(id);
		clientRepository.delete(existingClient);
		return existingClient;
	}
}