package com.ghostflyby.kotlin.plugin.fir

import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.hasModifier
import org.jetbrains.kotlin.fir.copyWithNewDefaults
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.utils.visibility
import org.jetbrains.kotlin.fir.extensions.FirStatusTransformerExtension
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.lexer.KtTokens

class DefaultInternalTransformer(session: FirSession) : FirStatusTransformerExtension(session) {
  override fun needTransformStatus(declaration: FirDeclaration): Boolean {
    return declaration is FirMemberDeclaration &&
        !declaration.symbol.hasModifier(KtTokens.PUBLIC_KEYWORD)
        && (declaration.status.visibility == Visibilities.Unknown || declaration.status.visibility == Visibilities.Public)
  }

  override fun transformStatus(status: FirDeclarationStatus, declaration: FirDeclaration): FirDeclarationStatus {
    return status.copyWithNewDefaults(
      visibility = Visibilities.Internal
    )
  }

  override fun transformStatus(
    status: FirDeclarationStatus,
    propertyAccessor: FirPropertyAccessor,
    containingClass: FirClassLikeSymbol<*>?,
    containingProperty: FirProperty?,
    isLocal: Boolean
  ): FirDeclarationStatus {
    return if (containingProperty != null)
      status.copyWithNewDefaults(
        visibility = containingProperty.visibility
      )
    else
      status
  }

}
