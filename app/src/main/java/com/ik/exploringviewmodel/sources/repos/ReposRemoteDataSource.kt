package com.ik.exploringviewmodel.sources.repos


import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.interceptors.loggingInterceptor
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_object
import com.ik.exploringviewmodel.entities.Repo
import io.reactivex.Single

/**
 * Created by ihor on 18.05.17.
 */
object ReposRemoteDataSource : ReposDataSource {

    init {
        FuelManager.instance.basePath = "https://api.github.com"
        FuelManager.instance.addRequestInterceptor(loggingInterceptor())
    }

    override fun getRepositories(organization: String): Single<List<Repo>> =
            "/orgs/$organization/repos"
                    .httpGet()
                    .rx_object(Repo.ListDeserializer())
                    .map { it?.component1() ?: throw it?.component2() ?: throw Exception() }

}