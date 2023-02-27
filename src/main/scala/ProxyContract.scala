package scenarios

import utils.ErgoScriptContract

import org.ergoplatform.appkit._
import org.ergoplatform.appkit.config.ErgoToolConfig

object ProxyContract {

    def main(args: Array[String]): Unit = {
        val address = getProxyContract("config.json")
        println("Proxy Contract Address: " + address)
    }

    def getProxyContract(configFilePath: String): Address = {
        val contract = ErgoScriptContract("src/main/resources/proxy.ergoscript").loadContract()

        val toolConfig = ErgoToolConfig.load(configFilePath)
        val nodeConfig = toolConfig.getNode()
        val ergoClient = RestApiErgoClient.create(nodeConfig, RestApiErgoClient.defaultTestnetExplorerUrl)
        val contractAddress = ergoClient.execute((ctx: BlockchainContext) => {
            val compiledContract = ctx.compileContract(
            ConstantsBuilder.empty(),
            contract
            )
            val contractAddress = Address.fromErgoTree(compiledContract.getErgoTree, ctx.getNetworkType)
            contractAddress
        })
        contractAddress
    }
}